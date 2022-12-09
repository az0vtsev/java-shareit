package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.validator.Validator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET /bookings/owner?state={} request received", stateParam);
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                             @Positive @PathVariable int bookingId) {
        log.info("GET /bookings/{} request received", bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid BookingDto bookingDto) {
        log.info("POST /bookings/ request received, userId={}", userId);
        Validator.checkDateTimeCreated(bookingDto.getStart(), bookingDto.getEnd());
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                                      @Positive @PathVariable int bookingId,
                                                      @RequestParam Boolean approved) {
        log.info("PATCH /bookings/{}?approved={} request received", bookingId, approved);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUserItemsBookings(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                                       @RequestParam(name = "state", defaultValue = "ALL")
                                                       String stateParam,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                       @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /bookings/owner?state={} request received", stateParam);
        BookingState bookingState = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getUserItemBookings(userId, bookingState, from, size);
    }

}

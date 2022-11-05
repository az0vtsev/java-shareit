package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingInfoDto createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST /bookings/ request received");
        BookingInfoDto createBookingDto = service.createBooking(userId, bookingDto);
        log.info("POST /bookings/ request done");
        return  createBookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingInfoDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int bookingId,
                              @RequestParam Boolean approved) {
        log.info("PATCH /bookings/{} request received", bookingId);
        BookingInfoDto updateBookingDto = service.updateBookingStatus(userId, bookingId, approved);
        log.info("PATCH /bookings/{} request done", bookingId);
        return updateBookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingInfoDto getBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                @PathVariable int bookingId) {
        log.info("GET /bookings/{} request received", bookingId);
        BookingInfoDto bookingDto = service.getBookingById(bookingId, userId);
        log.info("GET /bookings/{} request done", bookingId);
        return bookingDto;
    }

    @GetMapping
    public List<BookingInfoDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(required = false) Optional<String> state,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
        String stateParam = state.isEmpty() ? "ALL" : state.get();
        log.info("GET /bookings?state={} request received", state);
        List<BookingInfoDto> bookings = service.getUserBookings(userId, stateParam, from, size);
        log.info("GET /bookings?state={} request done", state);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingInfoDto> getUserItemsBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                     @RequestParam(required = false) Optional<String> state,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                     @Positive @RequestParam(defaultValue = "10") int size) {
        String stateParam = state.isEmpty() ? "ALL" : state.get();
        log.info("GET /bookings/owner?state={} request received", state);
        List<BookingInfoDto> itemsBookings = service.getUserItemsBookings(userId, stateParam, from, size);
        log.info("GET /bookings/owner?state={} request done", state);
        return itemsBookings;
    }
}

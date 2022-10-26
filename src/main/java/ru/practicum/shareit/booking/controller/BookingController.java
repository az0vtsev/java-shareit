package ru.practicum.shareit.booking.controller;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.*;



/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService service;

    @Autowired
    public BookingController(@Qualifier("bookingServiceImpl") BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingInfoDto createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @Valid @RequestBody BookingDto bookingDto)
            throws NotFoundException, NotValidDateException, UnsupportedBookingDataException {
        log.info("POST /bookings/ request received");
        BookingInfoDto createBookingDto = service.createBooking(userId, bookingDto);
        log.info("POST /bookings/ request done");
        return  createBookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingInfoDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int bookingId,
                              @RequestParam Boolean approved)
            throws NotFoundException, NotValidUserException, UnsupportedBookingDataException {
        log.info("PATCH /bookings/{} request received", bookingId);
        BookingInfoDto updateBookingDto = service.updateBookingStatus(userId, bookingId, approved);
        log.info("PATCH /bookings/{} request done", bookingId);
        return updateBookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingInfoDto getBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                @PathVariable int bookingId) throws NotFoundException, NotValidUserException {
        log.info("GET /bookings/{} request received", bookingId);
        BookingInfoDto bookingDto = service.getBookingById(bookingId, userId);
        log.info("GET /bookings/{} request done", bookingId);
        return bookingDto;
    }

    @GetMapping
    public List<BookingInfoDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @RequestParam(required = false) Optional<String> state)
            throws UnsupportedBookingStatusException, NotFoundException {
        String stateParam = state.isEmpty() ? "ALL" : state.get();
        log.info("GET /bookings?state={} request received", state);
        List<BookingInfoDto> bookings = service.getUserBookings(userId, stateParam);
        log.info("GET /bookings?state={} request done", state);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingInfoDto> getUserItemsBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(required = false) Optional<String> state)
            throws UnsupportedBookingStatusException, NotFoundException {
        String stateParam = state.isEmpty() ? "ALL" : state.get();
        log.info("GET /bookings/owner?state={} request received", state);
        List<BookingInfoDto> itemsBookings = service.getUserItemsBookings(userId, stateParam);
        log.info("GET /bookings/owner?state={} request done", state);
        return itemsBookings;
    }
}

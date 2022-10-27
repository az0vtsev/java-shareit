package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import java.util.List;

public interface BookingService {

    BookingInfoDto createBooking(Integer userId, BookingDto bookingDto);

    BookingInfoDto updateBookingStatus(Integer userId, Integer bookingId, Boolean approved);

    BookingInfoDto getBookingById(int id, int userId);

    List<BookingInfoDto> getUserBookings(int userId, String state);

    List<BookingInfoDto> getUserItemsBookings(int userId, String state);
}

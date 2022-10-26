package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.exception.*;

import java.util.List;

@Repository
public interface BookingService {

    BookingInfoDto createBooking(Integer userId, BookingDto bookingDto)
            throws NotFoundException, NotValidDateException, UnsupportedBookingDataException;

    BookingInfoDto updateBookingStatus(Integer userId, Integer bookingId, Boolean approved)
            throws NotFoundException, NotValidUserException, UnsupportedBookingDataException;

    BookingInfoDto getBookingById(int id, int userId) throws NotFoundException, NotValidUserException;

    List<BookingInfoDto> getUserBookings(int userId, String state)
            throws UnsupportedBookingStatusException, NotFoundException;

    List<BookingInfoDto> getUserItemsBookings(int userId, String state)
            throws UnsupportedBookingStatusException, NotFoundException;
}

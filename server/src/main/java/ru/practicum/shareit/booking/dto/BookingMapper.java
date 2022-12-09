package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

public class BookingMapper {
    public static BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingItemDto mapToBookingItemDto(Booking booking, ItemDto item, int bookerId) {
        return new BookingItemDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                item,
                bookerId,
                booking.getStatus()
        );
    }

    public static BookingInfoDto mapToBookingInfoDto(Booking booking, ItemDto item, UserDto booker) {
        return new BookingInfoDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                item,
                booker,
                booking.getStatus()
        );
    }

    public static Booking mapToBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBooker(),
                bookingDto.getStatus()
        );
    }
}

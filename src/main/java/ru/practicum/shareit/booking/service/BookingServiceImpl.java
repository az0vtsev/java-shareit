package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.NotValidUserException;
import ru.practicum.shareit.exception.UnsupportedBookingStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingStorage storage;
    private UserStorage userStorage;
    private ItemStorage itemStorage;

    @Autowired
    public BookingServiceImpl(BookingStorage storage, UserStorage userStorage, ItemStorage itemStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    public BookingInfoDto createBooking(Integer userId, BookingDto bookingDto) {
        Validator.checkUserExistence(userId, userStorage);
        int itemId = bookingDto.getItemId();
        Validator.checkItemBookingData(itemId,userId, itemStorage);
        Validator.checkDateTimeCreated(bookingDto.getStart(), bookingDto.getEnd());
        bookingDto.setBooker(userId);
        Booking createBooking = BookingMapper.mapToBooking(bookingDto);
        createBooking.setStatus(BookingStatus.WAITING);
        UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(userId).get());
        ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.findById(itemId).get());
        return BookingMapper.mapToBookingInfoDto(storage.save(createBooking), itemDto, userDto);
    }

    @Override
    public BookingInfoDto updateBookingStatus(Integer userId, Integer bookingId, Boolean approved) {
        Validator.checkUserExistence(userId, userStorage);
        Validator.checkBookingExistence(bookingId, storage);
        Booking booking = storage.findById(bookingId).get();
        Validator.checkBookingIsApproved(booking);
        int itemId = booking.getItem();
        Validator.checkUserIsOwner(itemId, userId, itemStorage);
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(booking.getBooker()).get());
        ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.findById(itemId).get());
        return BookingMapper.mapToBookingInfoDto(storage.save(booking), itemDto, userDto);
    }

    @Override
    public BookingInfoDto getBookingById(int id, int userId) {
        Validator.checkBookingExistence(id,storage);
        Booking booking = storage.findById(id).get();
        int booker = booking.getBooker();
        UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(booker).get());
        ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.findById(booking.getItem()).get());
        int owner = itemDto.getOwner();
        if ((userId != booker) && (userId != owner)) {
            throw new NotValidUserException("User id= " + userId + " don't have access");
        }
        return BookingMapper.mapToBookingInfoDto(booking, itemDto, userDto);
    }

    @Override
    public List<BookingInfoDto> getUserBookings(int userId, String state) {
        Validator.checkUserExistence(userId, userStorage);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = storage.findByBookerOrderByStartDesc(userId);
                break;
            case "WAITING":
                bookings = storage.findByBookerAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = storage.findByBookerAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            case "PAST":
                bookings = storage.findByBookerAndEndIsBeforeOrderByStartDesc(userId, dateTime);
                break;
            case "CURRENT" :
                bookings = storage.findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                        dateTime, dateTime);
                break;
            case "FUTURE":
                bookings = storage.findByBookerAndStartIsGreaterThanEqualOrderByStartDesc(userId, dateTime);
                break;
            default:
                throw new UnsupportedBookingStatusException("Unknown state: " + state);

        }
        List<BookingInfoDto> bookingsInfoDto = new ArrayList<>();
        for (Booking booking: bookings) {
            UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(booking.getBooker()).get());
            ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.findById(booking.getItem()).get());
            BookingInfoDto bookingInfoDto = BookingMapper.mapToBookingInfoDto(booking, itemDto, userDto);
            bookingsInfoDto.add(bookingInfoDto);
        }
        return bookingsInfoDto;
    }

    @Override
    public List<BookingInfoDto> getUserItemsBookings(int userId, String state) {
        Validator.checkUserExistence(userId, userStorage);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = storage.findUserBookings(userId);
                break;
            case "WAITING":
                bookings = storage.findUserBookingsByStatus(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = storage.findUserBookingsByStatus(userId, BookingStatus.REJECTED);
                break;
            case "PAST":
                bookings = storage.findUserBookingsPast(userId, dateTime);
                break;
            case "CURRENT":
                bookings = storage.findUserBookingsCurrent(userId, dateTime);
                break;
            case "FUTURE":
                bookings = storage.findUserBookingsFuture(userId, dateTime);
                break;
            default:
                throw new UnsupportedBookingStatusException("Unknown state: " + state);
        }
        List<BookingInfoDto> bookingsInfoDto = new ArrayList<>();
        for (Booking booking: bookings) {
            UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(booking.getBooker()).get());
            ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.findById(booking.getItem()).get());
            BookingInfoDto bookingInfoDto = BookingMapper.mapToBookingInfoDto(booking, itemDto, userDto);
            bookingsInfoDto.add(bookingInfoDto);
        }
        return bookingsInfoDto;
    }
}

package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public List<BookingInfoDto> getUserBookings(int userId, String state, int from, int size) {
        Validator.checkUserExistence(userId, userStorage);
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        LocalDateTime dateTime = LocalDateTime.now();
        Page<Booking> bookingsPage;
        switch (state) {
            case "ALL":
                bookingsPage = storage.findByBookerOrderByStartDesc(userId, pageRequest);
                break;
            case "WAITING":
                bookingsPage = storage.findByBookerAndStatusOrderByStartDesc(userId,
                                                                             BookingStatus.WAITING, pageRequest);
                break;
            case "REJECTED":
                bookingsPage = storage.findByBookerAndStatusOrderByStartDesc(userId,
                                                                             BookingStatus.REJECTED, pageRequest);
                break;
            case "PAST":
                bookingsPage = storage.findByBookerAndEndIsBeforeOrderByStartDesc(userId, dateTime, pageRequest);
                break;
            case "CURRENT":
                bookingsPage = storage.findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                        dateTime, dateTime, pageRequest);
                break;
            case "FUTURE":
                bookingsPage = storage.findByBookerAndStartIsGreaterThanEqualOrderByStartDesc(userId,
                        dateTime, pageRequest);
                break;
            default:
                throw new UnsupportedBookingStatusException("Unknown state: " + state);

        }
        List<BookingInfoDto> bookingsInfoDto = new ArrayList<>();
        for (Booking booking: bookingsPage.getContent()) {
            UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(booking.getBooker()).get());
            ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.findById(booking.getItem()).get());
            BookingInfoDto bookingInfoDto = BookingMapper.mapToBookingInfoDto(booking, itemDto, userDto);
            bookingsInfoDto.add(bookingInfoDto);
        }
        return bookingsInfoDto;
    }

    @Override
    public List<BookingInfoDto> getUserItemsBookings(int userId, String state, int from, int size) {
        Validator.checkUserExistence(userId, userStorage);
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        LocalDateTime dateTime = LocalDateTime.now();
        Page<Booking> bookingsPage;
        switch (state) {
            case "ALL":
                bookingsPage = storage.findUserBookings(userId, pageRequest);
                break;
            case "WAITING":
                bookingsPage = storage.findUserBookingsByStatus(userId, BookingStatus.WAITING, pageRequest);
                break;
            case "REJECTED":
                bookingsPage = storage.findUserBookingsByStatus(userId, BookingStatus.REJECTED, pageRequest);
                break;
            case "PAST":
                bookingsPage = storage.findUserBookingsPast(userId, dateTime, pageRequest);
                break;
            case "CURRENT":
                bookingsPage = storage.findUserBookingsCurrent(userId, dateTime, pageRequest);
                break;
            case "FUTURE":
                bookingsPage = storage.findUserBookingsFuture(userId, dateTime, pageRequest);
                break;
            default:
                throw new UnsupportedBookingStatusException("Unknown state: " + state);
        }
        List<BookingInfoDto> bookingsInfoDto = new ArrayList<>();
        for (Booking booking: bookingsPage.getContent()) {
            UserDto userDto = UserMapper.mapToUserDto(userStorage.findById(booking.getBooker()).get());
            ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.findById(booking.getItem()).get());
            BookingInfoDto bookingInfoDto = BookingMapper.mapToBookingInfoDto(booking, itemDto, userDto);
            bookingsInfoDto.add(bookingInfoDto);
        }
        return bookingsInfoDto;
    }
}

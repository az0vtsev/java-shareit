package ru.practicum.shareit.validator;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Validator {
    public static void checkUserExistence(int userId, UserStorage storage) {
        if (!storage.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " not found");
        }
    }

    public static void checkBookingExistence(int bookingId, BookingStorage storage) {
        if (!storage.existsById(bookingId)) {
            throw new NotFoundException("Booking id=" + bookingId + " not found");
        }
    }

    public static void ownerAuthorization(int itemId, int userId, UserStorage userStorage, ItemStorage itemStorage) {
        checkUserExistence(userId, userStorage);
        checkItemExistence(itemId, itemStorage);
        checkUserIsOwner(itemId, userId, itemStorage);

    }

    public static void checkBookingIsApproved(Booking booking) {
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new UnsupportedBookingDataException("Booking already approved");
        }
    }

    public static void checkUserIsOwner(int itemId, int userId, ItemStorage itemStorage) {
        if (itemStorage.findById(itemId).get().getOwner() != userId) {
            throw new NotValidUserException("User id=" + userId + " isn't owner of item id=" + itemId);
        }
    }

    public static void checkItemExistence(int itemId, ItemStorage storage) {
        if (!storage.existsById(itemId)) {
            throw new NotFoundException("Item id=" + itemId + " not found");
        }
    }

    public static void checkItemBookingData(int itemId, int userId, ItemStorage storage) {
        Optional<Item> result = storage.findById(itemId);
        if (result.isEmpty()) {
            throw new NotFoundException("Item id=" + itemId + " not found");
        } else {
            Item item = result.get();
            if (item.getOwner() == userId) {
                throw new NotFoundException("User id=" + userId + " is owner item id=" + itemId);
            }
            if (!item.getAvailable()) {
                throw new UnsupportedBookingDataException("Item id=" + itemId + " isn't available");
            }
        }
    }

    public static void checkEmailIsValid(String email) {
        if (!email.matches("^(.+)@(\\S+)$")) {
            throw new NotValidEmailException("Email isn't valid");
        }
    }

    public static void checkEmailIsUnique(String email, UserStorage storage) {
        if (isContainsEmail(email, storage)) {
            throw new NotUniqueEmailException("User with that email already exist");
        }
    }

    public static boolean isContainsEmail(String email, UserStorage storage) {
        return storage.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public static void checkDateTimeCreated(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new NotValidDateException("Date time isn't valid");
        }
    }

    public static void checkUserBookingItem(int userId, int itemId, BookingStorage bookingStorage) {
       List<Booking> result = bookingStorage
               .findByBookerAndItemAndEndIsBefore(userId, itemId, LocalDateTime.now());
        if (result.isEmpty()) {
            throw new NotValidAuthorCommentException("User id=" + userId + " isn't booker of item id=" + itemId);
        }
    }
}

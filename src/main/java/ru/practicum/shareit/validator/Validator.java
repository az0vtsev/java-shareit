package ru.practicum.shareit.validator;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.NotValidEmailException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

public class Validator {
    public static void checkUserExistence(int userId, UserStorage storage) throws NotFoundException {
            if (storage.getAll().stream().map(UserMapper::mapToUserDto).noneMatch(user -> user.getId() == userId)) {
                throw new NotFoundException("User id=" + userId + " not found");
            }
    }

    public static void ownerAuthorization(int itemId, int userId, UserStorage userStorage, ItemStorage itemStorage)
            throws NotItemOwnerException, NotFoundException {
        checkUserExistence(userId, userStorage);
        checkItemExistence(itemId, itemStorage);
        checkUserIsOwner(itemId, userId, itemStorage);

    }

    public static void checkUserIsOwner(int itemId, int userId, ItemStorage itemStorage)
            throws NotItemOwnerException {
        if (itemStorage.getById(itemId).getOwner() != userId) {
            throw new NotItemOwnerException("User id=" + userId + " isn't owner of item id=" + itemId);
        }
    }

    public static void checkItemExistence(int itemId, ItemStorage storage) throws NotFoundException {
        if (storage.getAll().stream().noneMatch(item -> item.getId() == itemId)) {
            throw new NotFoundException("Item id=" + itemId + " not found");
        }
    }

    public static void checkEmailIsValid(String email) throws NotValidEmailException {
        if (!email.matches("^(.+)@(\\S+)$")) {
            throw new NotValidEmailException("Email isn't valid");
        }
    }
    public static void checkEmailIsUnique(String email, UserStorage storage) throws NotUniqueEmailException {
        if (isContainsEmail(email, storage)) {
            throw new NotUniqueEmailException("User with that email already exist");
        }
    }
    public static boolean isContainsEmail(String email, UserStorage storage) {
        return storage.getAll().stream().anyMatch(user-> user.getEmail().equals(email));
    }
}

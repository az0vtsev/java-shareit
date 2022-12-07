package ru.practicum.shareit.validator;

import ru.practicum.shareit.exception.NotValidDateException;
import ru.practicum.shareit.exception.NotValidEmailException;

import java.time.LocalDateTime;

public class Validator {
    public static void checkEmailIsValid(String email) {
        if (!email.matches("^(.+)@(\\S+)$")) {
            throw new NotValidEmailException("Email isn't valid");
        }
    }

    public static void checkDateTimeCreated(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new NotValidDateException("Date time isn't valid");
        }
    }

}

package ru.practicum.shareit.exception;

public class NotValidEmailException extends RuntimeException {
    public NotValidEmailException(String message) {
        super(message);
    }
}

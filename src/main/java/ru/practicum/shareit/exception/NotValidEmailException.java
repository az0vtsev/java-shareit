package ru.practicum.shareit.exception;

public class NotValidEmailException extends Exception {
    public NotValidEmailException(String message) {
        super(message);
    }
}

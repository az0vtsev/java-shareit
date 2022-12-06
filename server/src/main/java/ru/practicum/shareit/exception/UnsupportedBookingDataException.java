package ru.practicum.shareit.exception;

public class UnsupportedBookingDataException extends RuntimeException {
    public UnsupportedBookingDataException(String message) {
        super(message);
    }
}

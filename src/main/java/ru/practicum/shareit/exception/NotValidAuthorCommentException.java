package ru.practicum.shareit.exception;

public class NotValidAuthorCommentException extends RuntimeException {
    public NotValidAuthorCommentException(String message) {
        super(message);
    }
}

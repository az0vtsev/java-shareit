package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.user.controller.UserController;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {BookingController.class, ItemController.class,
        UserController.class, ItemRequestController.class})
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Data isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotValidAuthorCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidAuthorCommentException(final NotValidAuthorCommentException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Data isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(UnsupportedBookingDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedBookingDataException(final UnsupportedBookingDataException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Data isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotValidDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidDateException(final NotValidDateException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Data isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(UnsupportedBookingStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedBookingStatusException(final UnsupportedBookingStatusException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Unknown state: UNSUPPORTED_STATUS",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotValidUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotValidUserException(final NotValidUserException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Data isn't valid",
                "errorMessage", e.getMessage()
        );
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Entity not found",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotUniqueEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleNotUniqueEmailException(final NotUniqueEmailException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Email already exist",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotValidEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidEmailException(final NotValidEmailException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Email isn't valid",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final RuntimeException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Runtime Exception",
                "errorMessage", e.getMessage()
        );
    }
/*
    @ExceptionHandler(NotItemOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleNotItemOwnerException(final NotItemOwnerException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Not item owner",
                "errorMessage", e.getMessage()
        );
    }

 */


}
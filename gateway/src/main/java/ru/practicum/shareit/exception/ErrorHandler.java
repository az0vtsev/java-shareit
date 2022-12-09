package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.user.UserController;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {BookingController.class,  UserController.class})
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedBookingStatusException(final IllegalArgumentException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Unknown state: UNSUPPORTED_STATUS",
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

    @ExceptionHandler(NotValidEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidEmailException(final NotValidEmailException e) {
        log.error(e.getMessage());
        return Map.of(
                "error", "Email isn't valid",
                "errorMessage", e.getMessage()
        );
    }
}

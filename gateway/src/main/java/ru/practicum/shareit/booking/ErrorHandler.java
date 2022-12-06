package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {BookingController.class})
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
}

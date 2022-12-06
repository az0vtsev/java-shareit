package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private int id;
    @FutureOrPresent
    @NotNull
    private LocalDateTime start;
    @FutureOrPresent
    @NotNull
    private LocalDateTime end;
    @NotNull
    private Integer itemId;
    private Integer booker;
    private BookingState status;
}
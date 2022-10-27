package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemInfoDto {
    private final int id;
    @NotBlank(message = "Owner is required")
    private final UserDto owner;
    @NotBlank(message = "Item name is required")
    private String name;
    @NotBlank(message = "Item description is required")
    private String description;
    @NotNull(message = "Available is required")
    private Boolean available;
    private Integer request;
    private List<CommentDto> comments;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;

}

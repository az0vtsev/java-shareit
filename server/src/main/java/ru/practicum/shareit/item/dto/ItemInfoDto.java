package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemInfoDto {
    private final int id;
    private final UserDto owner;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private List<CommentDto> comments;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;

}

package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;

public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                new ArrayList<>());
    }

    public static Item mapToItem(ItemDto itemDto) {
       return new Item(
               itemDto.getId(),
               itemDto.getOwner(),
               itemDto.getName(),
               itemDto.getDescription(),
               itemDto.getAvailable(),
               itemDto.getRequest()
       );
    }

    public static ItemInfoDto mapToItemInfoDto(Item item, UserDto owner,
                                               BookingItemDto bookingPrev, BookingItemDto bookingNext) {
        return new ItemInfoDto(
                item.getId(),
                owner,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                new ArrayList<>(),
                bookingPrev,
                bookingNext
        );
    }
}

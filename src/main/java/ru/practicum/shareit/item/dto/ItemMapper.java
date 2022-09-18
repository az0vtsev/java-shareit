package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest());
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
}

package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto) throws NotFoundException;

    ItemDto updateItem(ItemDto itemDto) throws NotFoundException, NotItemOwnerException;

    void deleteItem(int itemId, int ownerId)  throws NotFoundException, NotItemOwnerException;

    ItemDto getItemById(int id) throws NotFoundException;

    List<ItemDto> getItemsByOwner(int ownerId);

    List<ItemDto> getItemsBySearch(String text);
}

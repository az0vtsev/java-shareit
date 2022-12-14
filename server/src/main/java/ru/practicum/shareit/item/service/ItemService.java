package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto);

    void deleteItem(int itemId, int ownerId);

    ItemInfoDto getItemById(int id, int userId);

    List<ItemInfoDto> getItemsByOwner(int ownerId, int from, int size);

    List<ItemDto> getItemsBySearch(String text, int from, int size);

    CommentDto addComment(int itemId, int userId, CommentDto commentDto);
}

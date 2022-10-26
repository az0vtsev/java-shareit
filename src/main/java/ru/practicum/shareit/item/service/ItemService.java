package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.NotValidAuthorCommentException;
import ru.practicum.shareit.exception.NotValidUserException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto) throws NotFoundException;

    ItemDto updateItem(ItemDto itemDto) throws NotFoundException, NotItemOwnerException, NotValidUserException;

    void deleteItem(int itemId, int ownerId) throws NotFoundException, NotItemOwnerException, NotValidUserException;

    ItemInfoDto getItemById(int id, int userId) throws NotFoundException;

    List<ItemInfoDto> getItemsByOwner(int ownerId);

    List<ItemDto> getItemsBySearch(String text);

    CommentDto addComment(int itemId, int userId, CommentDto commentDto)
            throws NotFoundException, NotValidAuthorCommentException;
}

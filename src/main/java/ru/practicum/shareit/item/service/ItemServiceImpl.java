package ru.practicum.shareit.item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validator.Validator;

@Service("itemServiceImpl")
public class ItemServiceImpl implements ItemService {
    private ItemStorage storage;
    private UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(@Qualifier("itemInMemoryStorage") ItemStorage storage,
                           @Qualifier("userInMemoryStorage") UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto) throws NotFoundException {
        Validator.checkUserExistence(itemDto.getOwner(), userStorage);
        Item createItem = ItemMapper.mapToItem(itemDto);
        return ItemMapper.mapToItemDto(storage.create(createItem));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) throws NotFoundException, NotItemOwnerException {
        Validator.ownerAuthorization(itemDto.getId(), itemDto.getOwner(), userStorage, storage);
        ItemDto oldItem = getItemById(itemDto.getId());
        prepareToUpdate(itemDto, oldItem);
        Item updateItem = ItemMapper.mapToItem(itemDto);
        return ItemMapper.mapToItemDto(storage.update(updateItem));
    }

    @Override
    public void deleteItem(int itemId, int ownerId) throws NotFoundException, NotItemOwnerException {
        Validator.ownerAuthorization(itemId, ownerId, userStorage, storage);
        storage.delete(itemId);
    }

    @Override
    public ItemDto getItemById(int id) throws NotFoundException {
        Validator.checkItemExistence(id, storage);
        return ItemMapper.mapToItemDto(storage.getById(id));
    }

    @Override
    public List<ItemDto> getItemsByOwner(int ownerId) {
        return storage.getAll()
                .stream()
                .filter(item -> item.getOwner() == ownerId)
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return getItemsByText(text);
    }

    private List<ItemDto> getItemsByText(String text) {
        return storage.getAll()
                .stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains((text.toLowerCase())))
                        && item.getAvailable())
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void prepareToUpdate(ItemDto updateItem, ItemDto oldItem) {
        if (updateItem.getName() == null) {
            updateItem.setName(oldItem.getName());
        }
        if (updateItem.getDescription() == null) {
            updateItem.setDescription(oldItem.getDescription());
        }
        if (updateItem.getAvailable() == null) {
            updateItem.setAvailable(oldItem.getAvailable());
        }
    }
}

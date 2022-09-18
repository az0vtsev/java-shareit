package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);
    Item update(Item item);
    Item delete(int id);
    Item getById(int id);
    List<Item> getAll();

}

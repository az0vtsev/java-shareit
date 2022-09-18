package ru.practicum.shareit.item.storage;

import java.util.List;
import ru.practicum.shareit.item.model.Item;

public interface ItemStorage {

    Item create(Item item);

    Item update(Item item);

    Item delete(int id);

    Item getById(int id);

    List<Item> getAll();
}

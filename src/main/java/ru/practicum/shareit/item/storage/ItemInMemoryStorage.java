package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("itemInMemoryStorage")
public class ItemInMemoryStorage {
    private int nextId = 0;
    private HashMap<Integer, Item> items = new HashMap<>();

    private Integer getNextId() {
        return ++nextId;
    }

    public Item create(Item item) {
        int itemId = getNextId();
        Item newItem = new Item(itemId, item.getOwner(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequest());
        items.put(itemId, newItem);
        return items.get(newItem.getId());
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    public Item delete(int id) {
        return items.remove(id);
    }

    public Item getById(int id) {
        return items.get(id);
    }

    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }
}

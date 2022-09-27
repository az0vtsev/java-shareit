package ru.practicum.shareit.item.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository("itemInMemoryStorage")
public class ItemInMemoryStorage implements ItemStorage {
    private int nextId = 0;
    private HashMap<Integer, Item> items = new HashMap<>();

    private Integer getNextId() {
        return ++nextId;
    }

    @Override
    public Item create(Item item) {
        int itemId = getNextId();
        Item newItem = new Item(itemId, item.getOwner(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequest());
        items.put(itemId, newItem);
        return items.get(newItem.getId());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item delete(int id) {
        return items.remove(id);
    }

    @Override
    public Item getById(int id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }
}

package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("userInMemoryStorage")
public class UserInMemoryStorage {
    private int nextId = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    private Integer getNextId() {
        return ++nextId;
    }

    public User getById(int id) {
        return users.get(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        int userId = getNextId();
        User newUser = new User(userId, user.getName(), user.getEmail());
        users.put(userId, newUser);
        return users.get(newUser.getId());
    }

    public User update(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public User delete(int id) {
        return users.remove(id);
    }
}

package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User getById(int id);
    List<User> getAll();
    User create(User user);
    User update(User user);
    User delete(int id);
}

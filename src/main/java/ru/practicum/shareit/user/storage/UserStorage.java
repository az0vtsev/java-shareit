package ru.practicum.shareit.user.storage;

import java.util.List;
import ru.practicum.shareit.user.model.User;

public interface UserStorage {

    User getById(int id);

    List<User> getAll();

    User create(User user);

    User update(User user);

    User delete(int id);
}

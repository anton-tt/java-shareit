package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import java.util.List;

public interface UserStorage {

    User create(User user);
    User getById(long id);
    List<User> getAll();
    User update(User user);
    void delete(long id);

}
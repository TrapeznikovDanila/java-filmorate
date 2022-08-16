package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User addUser(User user);

    User updateUser(User user);

    void isValid(User user);

    HashMap<Long, User> getUsers();
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {

    List<User> getAll();

    User addUser(User user);

    User updateUser(User user);

    Map<Long, User> getUsers();

    Optional<User> getUserById(long id);
}

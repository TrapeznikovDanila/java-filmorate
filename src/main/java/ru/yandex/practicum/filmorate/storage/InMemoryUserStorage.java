package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ObjectConflictException;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    protected Map<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    private long id = 0;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        if (users.containsValue(user)) {
            throw new ObjectConflictException("This user was already added");
        }
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        log.info("Added a new user with id " + user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("The user with the id " + user.getId() + "is missing from the database");
            throw new ObjectNotFoundException("The user with the id " + user.getId() + "is missing from the database");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("Updated a user with id " + user.getId());
        users.put(user.getId(), user);
        return user;
    }


    @Override
    public Map<Long, User> getUsers() {
        return users;
    }
}

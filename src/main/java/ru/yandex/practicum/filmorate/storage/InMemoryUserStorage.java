package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    protected HashMap<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    private int id = 0;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        isValid(user);
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
        isValid(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("Updated a user with id " + user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void isValid(User user) {
        if ((user.getEmail() == null) || (!user.getEmail().contains("@"))) {
            throw new ValidationException("The email cannot be empty and must contain the character @");
        } else if ((user.getLogin() == null) || (user.getLogin().contains(" "))) {
            throw new ValidationException("The login cannot be empty and contain spaces");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("The date of birth cannot be in the future");
        }
    }

    @Override
    public HashMap<Long, User> getUsers() {
        return users;
    }
}

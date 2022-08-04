package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private int id = 0;

    @GetMapping
    public List<User> getUsers() {
        List<User> usersList = new ArrayList<>();
        for (User user : users.values()) {
            usersList.add(user);
        }
        return usersList;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (isValid(user)) {
            if ((user.getName() == null) || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            id++;
            user.setId(id);
            log.info("Добавлен новый пользователь с id" + user.getId());
            users.put(user.getId(), user);
            return user;
        } else {
            log.info("Введены некорректные данные");
            throw new ValidationException("Введены некорректные данные");
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (isValid(user)) {
                if (user.getName() == null) {
                    user.setName(user.getLogin());
                }
                log.info("Добавлен новый пользователь с id" + user.getId());
                users.put(user.getId(), user);
                return user;
            } else {
                log.info("Введены некорректные данные");
                throw new ValidationException("Введены некорректные данные");
            }
        }
        log.info("Введены некорректные данные");
        throw new ValidationException("Введены некорректные данные");
    }

    private boolean isValid(User user) {
        if ((user.getEmail() != null) && (user.getEmail().contains("@"))
                && (user.getLogin() != null) && (!user.getLogin().contains(" "))
                && user.getBirthday().isBefore(LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }
}

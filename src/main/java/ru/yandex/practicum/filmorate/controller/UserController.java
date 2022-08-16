package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private int id = 0;

    @Override
    @PostMapping
    public User addObject(@Valid @RequestBody User user) {
        super.addObject(user);
        if ((user.getName() == null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        log.info("Added a new user with id " + user.getId());
        objects.put(user.getId(), user);
        return user;
    }

    @Override
    @PutMapping
    public User updateObject(@Valid @RequestBody User user) {
        if (!objects.containsKey(user.getId())) {
            log.info("The user with the id " + user.getId() + "is missing from the database");
            throw new ValidationException("The user with the id " + user.getId() + "is missing from the database");
        }
        super.updateObject(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("Updated a user with id " + user.getId());
        objects.put(user.getId(), user);
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
}

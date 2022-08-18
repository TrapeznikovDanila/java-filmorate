package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    public User user = new User();
    public UserStorage userStorage = new InMemoryUserStorage();
    public UserService userService = new UserService(userStorage);

    @BeforeEach
    public void setUserInfo() {
        user.setName("name");
        user.setBirthday(LocalDate.of(1992, 12, 4));
        user.setLogin("login");
        user.setEmail("1111");
        user.setEmail("t@mail.ru");
    }

    @Test
    public void addUserWithCorrectData() {
        assertEquals(userService.addUser(user), user);
    }

    @Test
    public void addUserWithWrongEmail() {
        user.setEmail(null);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userService.isValid(user);
                });

        assertEquals("The email cannot be empty and must contain the character @", exception.getMessage());
    }

    @Test
    public void addUserWithWrongLogin() {
        user.setLogin("log in");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userService.isValid(user);
                });

        assertEquals("The login cannot be empty and contain spaces", exception.getMessage());
    }

    @Test
    public void addUserWithWrongBirthDay() {
        user.setBirthday(LocalDate.of(2023, 12, 4));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userService.isValid(user);
                });

        assertEquals("The date of birth cannot be in the future", exception.getMessage());
    }

    @Test
    public void addUserWithoutName() {
        user.setName(null);

        assertEquals("login", userService.addUser(user).getName());
    }
}

package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

public class UserControllerTest {

    public User user = new User();
    public UserStorage userStorage = new InMemoryUserStorage();

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
        assertEquals(userStorage.addUser(user), user);
    }

    @Test
    public void addUserWithWrongEmail() {
        user.setEmail(null);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userStorage.addUser(user);
                });

        assertEquals("The email cannot be empty and must contain the character @", exception.getMessage());
    }

    @Test
    public void addUserWithWrongLogin() {
        user.setLogin("log in");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userStorage.addUser(user);
                });

        assertEquals("The login cannot be empty and contain spaces", exception.getMessage());
    }

    @Test
    public void addUserWithWrongBirthDay() {
        user.setBirthday(LocalDate.of(2023, 12, 4));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userStorage.addUser(user);
                });

        assertEquals("The date of birth cannot be in the future", exception.getMessage());
    }

    @Test
    public void addUserWithoutName() {
        user.setName(null);

        assertEquals("login", userStorage.addUser(user).getName());
    }
}

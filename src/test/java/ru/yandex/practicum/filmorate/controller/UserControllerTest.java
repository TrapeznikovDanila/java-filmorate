package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {

    @Test
    public void addUserWithWrongEmail() {
        User user = new User();
        user.setName("name");
        user.setBirthday(LocalDate.of(1992,12,4));
        user.setLogin("login");
        user.setEmail("1111");

        UserController userController = new UserController();

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userController.addUser(user);;
                });

        assertEquals("Введены некорректные данные", exception.getMessage());
    }

    @Test
    public void addUserWithWrongLogin() {
        User user = new User();
        user.setName("name");
        user.setBirthday(LocalDate.of(1992,12,4));
        user.setLogin("log in");
        user.setEmail("t@mail.ru");

        UserController userController = new UserController();

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userController.addUser(user);;
                });

        assertEquals("Введены некорректные данные", exception.getMessage());
    }

    @Test
    public void addUserWithWrongBirthDay() {
        User user = new User();
        user.setName("name");
        user.setBirthday(LocalDate.of(2023,12,4));
        user.setLogin("login");
        user.setEmail("t@mail.ru");

        UserController userController = new UserController();

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    userController.addUser(user);;
                });

        assertEquals("Введены некорректные данные", exception.getMessage());
    }

    @Test
    public void addUserWithoutName() {
        User user = new User();
        user.setBirthday(LocalDate.of(1992,12,4));
        user.setLogin("login");
        user.setEmail("t@mail.ru");

        UserController userController = new UserController();

        assertEquals("login", userController.addUser(user).getName());
    }

    @Test
    public void addUserWithCorrectData() {
        User user = new User();
        user.setBirthday(LocalDate.of(1992,12,4));
        user.setName("name");
        user.setLogin("login");
        user.setEmail("t@mail.ru");

        UserController userController = new UserController();

        assertEquals(userController.addUser(user), user);
    }
}

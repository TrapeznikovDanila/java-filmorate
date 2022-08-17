package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class UserValidator {
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

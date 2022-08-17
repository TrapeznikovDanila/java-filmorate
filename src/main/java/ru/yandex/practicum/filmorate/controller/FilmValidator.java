package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@Component
public class FilmValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    public void isValid(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("The title of the movie cannot be empty");
        } else if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("The maximum length of a movie description is 200 characters");
        } else if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("The release date of the film cannot be earlier than December 28, 1895");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("The duration of the film should be positive");
        }
    }
}

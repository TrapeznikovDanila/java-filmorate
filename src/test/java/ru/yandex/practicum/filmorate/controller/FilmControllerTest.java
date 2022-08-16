package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {

    public Film film = new Film();
    public FilmController filmController = new FilmController();

    @BeforeEach
    public void setFilmInfo() {
        film.setName("name");
        film.setDescription("description");
        film.setDuration(80);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void addFilmWithCorrectData() {
        assertEquals(filmController.addObject(film), film);
    }

    @Test
    public void testWithExtremeCases() {
        char[] desriptionList = new char[200];
        for (int i = 0; i < desriptionList.length; i++) {
            desriptionList[i] = 'o';
        }
        String description = new String(desriptionList);
        film.setDescription(description);
        film.setDuration(1);

        assertEquals(film, filmController.addObject(film));
    }

    @Test
    public void addFilmWithoutNameTest() {
        film.setName("");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.isValid(film);
                });

        assertEquals("The title of the movie cannot be empty", exception.getMessage());
    }

    @Test
    public void addFilmWithLongDescription() {
        char[] desriptionList = new char[201];
        for (int i = 0; i < desriptionList.length; i++) {
            desriptionList[i] = 'o';
        }
        String description = new String(desriptionList);
        film.setDescription(description);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.isValid(film);
                });

        assertEquals("The maximum length of a movie description is 200 characters", exception.getMessage());
    }

    @Test
    public void addFilmWithWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1894, 1, 1));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.isValid(film);
                });

        assertEquals("The release date of the film cannot be earlier than December 28, 1895",
                exception.getMessage());
    }

    @Test
    public void addFilmWithNegativeDuration() {
        film.setDuration(-80);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.isValid(film);
                });

        assertEquals("The duration of the film should be positive", exception.getMessage());
    }
}

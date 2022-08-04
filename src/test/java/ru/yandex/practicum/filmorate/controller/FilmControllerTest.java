package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {

    @Test
    public void addFilmWithoutNameTest() {
        Film film = new Film();
        film.setName("");
        film.setDescription("description");
        film.setDuration(80);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        FilmController filmController = new FilmController();



        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.addFilm(film);;
                });

        assertEquals("Введены некорректные данные", exception.getMessage());
    }

    @Test
    public void addFilmWithLongDescription() {
        char[] desriptionList = new char[201];
        for (int i =0; i <=200; i++) {
            desriptionList[i] = 'o';
        }
        String description = new String(desriptionList);
        Film film = new Film();
        film.setName("name");
        film.setDescription(description);
        film.setDuration(80);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        FilmController filmController = new FilmController();

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.addFilm(film);;
                });

        assertEquals("Введены некорректные данные", exception.getMessage());
    }

    @Test
    public void addFilmWithWrongReleaseDate() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setDuration(80);
        film.setReleaseDate(LocalDate.of(1894,1,1));

        FilmController filmController = new FilmController();

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.addFilm(film);;
                });

        assertEquals("Введены некорректные данные", exception.getMessage());
    }

    @Test
    public void addFilmWithNegativeDuration() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setDuration(-80);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        FilmController filmController = new FilmController();

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> {
                    filmController.addFilm(film);;
                });

        assertEquals("Введены некорректные данные", exception.getMessage());
    }

    @Test
    public void addFilmWithCorrectData() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setDuration(80);
        film.setReleaseDate(LocalDate.of(2000,1,1));

        FilmController filmController = new FilmController();

        assertEquals(filmController.addFilm(film), film);
    }
}

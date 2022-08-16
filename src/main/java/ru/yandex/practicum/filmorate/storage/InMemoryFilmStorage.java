package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected HashMap<Long, Film> films = new HashMap<>();
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private int id = 0;
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        isValid(film);
        id++;
        film.setId(id);
        log.info("Added a new film with id " + film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("The film with the id " + film.getId() + "is missing from the database");
            throw new ObjectNotFoundException("The film with the id " + film.getId() + "is missing from the database");
        }
        isValid(film);
        log.info("Updated a film with id " + film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
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

    @Override
    public HashMap<Long, Film> getFilms() {
        return films;
    }
}

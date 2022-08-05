package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;

@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private int id = 0;

    @Override
    @PostMapping
    public Film addObject(@Valid @RequestBody Film film) {
        super.addObject(film);
        id++;
        film.setId(id);
        log.info("Added a new film with id " + film.getId());
        objects.put(film.getId(), film);
        return film;
    }

    @Override
    @PutMapping
    public Film updateObject(@Valid @RequestBody Film film) {
        if (!objects.containsKey(film.getId())) {
            log.info("The film with the id " + film.getId() + "is missing from the database");
            throw new ValidationException("The film with the id " + film.getId() + "is missing from the database");
        }
        super.updateObject(film);
        log.info("Updated a film with id " + film.getId());
        objects.put(film.getId(), film);
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
}

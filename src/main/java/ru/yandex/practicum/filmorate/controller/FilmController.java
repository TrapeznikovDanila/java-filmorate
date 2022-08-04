package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private int id = 0;

    @GetMapping
    public List<Film> getFilms() {
        List<Film> filmsList = new ArrayList<>();
        for (Film film : films.values()) {
            filmsList.add(film);
        }
        return filmsList;
    }

    @PostMapping
    public Film addFilm(@Valid  @RequestBody Film film) {
        if (isValid(film)) {
            id++;
            film.setId(id);
            log.info("Добавлен новый фильм с id" + film.getId());
            films.put(film.getId(), film);
            return film;
        } else {
            log.info("Введены некорректные данные");
            throw new ValidationException("Введены некорректные данные");
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            if (isValid(film)) {
                log.info("Обновлена информация о пользователе с id" + film.getId());
                films.put(film.getId(), film);
                return film;
            } else {
                log.info("Введены некорректные данные");
                throw new ValidationException("Введены некорректные данные");
            }
        }
        log.info("Введены некорректные данные");
        throw new ValidationException("Введены некорректные данные");
    }

    private boolean isValid(Film film) {
        if (!film.getName().isBlank() && (film.getDescription().length() <= 200)
        && (film.getReleaseDate().isAfter(CINEMA_BIRTHDAY))
        && (film.getDuration() > 0)) {
            return true;
        } else {
            return false;
        }
    }
}

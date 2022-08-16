package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

    List<Film> getAll();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void isValid(Film film);

    HashMap<Long, Film> getFilms();
}

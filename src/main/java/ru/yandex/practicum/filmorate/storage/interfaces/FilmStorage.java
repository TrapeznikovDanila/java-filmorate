package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    List<Film> getAll();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Map<Long, Film> getFilms();

    Film getFilmById(long id);
}

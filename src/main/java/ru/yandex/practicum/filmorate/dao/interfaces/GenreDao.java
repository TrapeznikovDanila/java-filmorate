package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    Genre getGenre(int id);

    List<Genre> getGenresList();
}

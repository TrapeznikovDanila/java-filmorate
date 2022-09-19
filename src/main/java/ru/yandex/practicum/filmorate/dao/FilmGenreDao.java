package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface FilmGenreDao {

    List<Integer> getFilmGenres(long id);

    void setFilmsGenres(long filmId, List<Integer> genresList);
}

package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ObjectConflictException;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private long id = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        checkFilm(film);
        if (filmStorage.getFilms().containsValue(film)) {
            throw new ObjectConflictException("This film was already added");
        }
        film.setId(++id);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        checkFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Film addLike(long id, long userId) {
        isFilmInMemory(id);
        if (getFilmById(id).getLikes().contains(userId)) {
            throw new ValidationException(String.format("User id %d has already liked film id %d", userId, id));
        }
        getFilmById(id).getLikes().add(userId);
        getFilmById(id).setRate(getFilmById(id).getRate() + 1);
        return getFilmById(id);
    }

    public Film deleteLike(long id, long userId) {
        isFilmInMemory(id);
        if (!getFilmById(id).getLikes().contains(userId)) {
            throw new ObjectNotFoundException(String.format("User id %d didn't like film id %d", userId, id));
        }
        getFilmById(id).getLikes().remove(userId);
        getFilmById(id).setRate(getFilmById(id).getRate() - 1);
        return getFilmById(id);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted((p0, p1) -> {
                    int comp = ((Integer) p0.getRate()).compareTo(p1.getRate()) * -1;
                    return comp;
                }).limit(count).collect(Collectors.toList());
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    private void isFilmInMemory(long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("Film id %d not found", id));
        }
    }

    public Film getFilmById(long id) {
        isFilmInMemory(id);
        return filmStorage.getFilms().get(id);
    }

    public void checkFilm(Film film) {
        if (film != null) {
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
}

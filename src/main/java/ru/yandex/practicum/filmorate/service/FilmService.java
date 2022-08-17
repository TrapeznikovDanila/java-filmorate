package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
                    int comp = p0.getRate().compareTo(p1.getRate()) * -1;
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
}

package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ObjectConflictException;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmGenreDao filmGenreDao;
    private final UserService userService;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);


    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       MpaDao mpaDao,
                       GenreDao genreDao,
                       FilmGenreDao filmGenreDao,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.filmGenreDao = filmGenreDao;
        this.userService = userService;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        checkFilm(film);
        if (filmStorage.getFilms().containsValue(film)) {
            throw new ObjectConflictException("This film was already added");
        }
        Film addedFilm = filmStorage.addFilm(film);
        setFilmGenre(addedFilm);
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        checkFilm(film);
        isFilmInMemory(film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        film.setGenres(deleteRepeatGenres(film));
        setFilmGenre(film);
        return updatedFilm;
    }

    public Film addLike(long id, long userId) {
        isFilmInMemory(id);
        userService.isUserInMemory(userId);
        Film film = getFilmById(id);
        film.getLikes().add(userId);
        film.setRate(film.getRate() + 1);
        updateFilm(film);
        return film;
    }

    public Film deleteLike(long id, long userId) {
        isFilmInMemory(id);
        userService.isUserInMemory(userId);
        Film film = getFilmById(id);
        film.getLikes().remove(userId);
        film.setRate(film.getRate() - 1);
        updateFilm(film);
        return film;
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
        if (id < 0 || filmStorage.getFilmById(id) == null) {
            log.info("The film with the id " + id + "is missing from the database");
            throw new ObjectNotFoundException(String.format("Film id %d not found", id));
        }
    }

    public Film getFilmById(long id) {
        isFilmInMemory(id);
        Film film = filmStorage.getFilmById(id);
        film.setGenres(getFilmGenres(id));
        return film;
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

    public List<Mpa> getMpaList() {
        return mpaDao.getMpaList();
    }

    public Mpa getMpa(int id) {
        return mpaDao.getMpa(id);
    }

    public List<Genre> getGenresList() {
        return genreDao.getGenresList();
    }

    public Genre getGenre(int id) {
        return genreDao.getGenre(id);
    }

    private void setFilmGenre(Film film) {
        List<Integer> genreIdList = film.getGenres().stream()
                .map(Genre::getId).collect(Collectors.toList());
        filmGenreDao.setFilmsGenres(film.getId(), genreIdList);
    }

    private List<Genre> getFilmGenres(long id) {
        return filmGenreDao.getFilmGenres(id).stream()
                .map(i -> genreDao.getGenre(i))
                .collect(Collectors.toList());
    }

    private List<Genre> deleteRepeatGenres(Film film) {
        Set<Genre> genresSet = new LinkedHashSet<>(film.getGenres());
        return new ArrayList<>(genresSet);
    }
}

package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmGenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTests {
    private final FilmDbStorage filmStorage;

    private final FilmGenreDaoImpl filmGenresDao;

    @Test
    public void testFindFilmById() {
        Film film1 = new Film();
        film1.setName("name");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.of(1999, 4, 1));
        film1.setDuration(120);
        film1.setRate(10);
        Mpa mpa = Mpa.builder().id(1).build();
        film1.setMpa(mpa);
        filmStorage.addFilm(film1);


        Film film = filmStorage.getFilmById(1);

        assertThat(film)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testUpdateFilm() {
        Film film1 = new Film();
        film1.setName("name");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.of(1999, 4, 1));
        film1.setDuration(120);
        film1.setRate(10);
        Mpa mpa = Mpa.builder().id(1).build();
        film1.setMpa(mpa);
        long filmId = filmStorage.addFilm(film1).getId();

        film1.setName("newName");
        film1.setId(filmId);

        filmStorage.updateFilm(film1);

        Film film = filmStorage.getFilmById(filmId);

        assertThat(film)
                .hasFieldOrPropertyWithValue("name", "newName");
    }

    @Test
    public void testGetFilms() {
        Film film1 = new Film();
        film1.setName("name");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.of(1999, 4, 1));
        film1.setDuration(120);
        film1.setRate(10);
        Mpa mpa = Mpa.builder().id(1).build();
        film1.setMpa(mpa);
        filmStorage.addFilm(film1);

        List<Film> filmsList = filmStorage.getAll();

        assertThat(filmsList).hasSize(1);
    }

    @Test
    public void setAndGetGenres() {
        Film film1 = new Film();
        film1.setName("name");
        film1.setDescription("description");
        film1.setReleaseDate(LocalDate.of(1999, 4, 1));
        film1.setDuration(120);
        film1.setRate(10);
        Mpa mpa = Mpa.builder().id(1).build();
        film1.setMpa(mpa);
        long filmId = filmStorage.addFilm(film1).getId();

        List<Integer> genresList = new ArrayList<>();
        genresList.add(1);

        filmGenresDao.setFilmsGenres(filmId, genresList);

        List<Integer> genresList2 = filmGenresDao.getFilmGenres(filmId);

        assertThat(genresList2).hasSize(1);
    }
}

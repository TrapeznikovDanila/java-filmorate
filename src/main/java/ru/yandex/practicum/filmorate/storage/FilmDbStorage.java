package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select * from films as f left join films_mpa as fm on " +
                "f.mpa_id = fm.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        log.info("Added a new film with id " + film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "film_name = ?, film_description = ?, film_releasedate = ?, film_duration = ?, " +
                "film_rate = ?, mpa_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        log.info("Updated a film with id " + film.getId());
        return film;
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> films = new HashMap<>();
        List<Film> userList = getAll();
        for (Film film : userList) {
            films.put(film.getId(), film);
        }
        return films;
    }

    @Override
    public Film getFilmById(long id) {
        String sqlQuery = "select * from films as f left join films_mpa as fm on " +
                "f.mpa_id = fm.mpa_id  where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("film_name"));
        film.setDescription(resultSet.getString("film_description"));
        film.setReleaseDate(resultSet.getTimestamp("film_releasedate").toLocalDateTime().toLocalDate());
        film.setDuration(resultSet.getInt("film_duration"));
        film.setRate(resultSet.getInt("film_rate"));
        film.setMpa(Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build());
        film.setLikes(makeLikes(film.getId()));
        return film;
    }

    private Set<Long> makeLikes(Long filmId) {
        Set<Long> likesSet = new HashSet<>();
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select * from user_likes where film_id = ?", filmId);
        while (likesRows.next()) {
            likesSet.add(likesRows.getLong("user_id"));
        }
        return likesSet;
    }
}

package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.dao.interfaces.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {

    private static final Logger log = LoggerFactory.getLogger(GenreDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genres where genre_id = ?",
                id);
        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("genre_id"));
            genre.setName(genreRows.getString("genre_name"));
            return genre;
        } else {
            log.info("Genre id %d not found", id);
            throw new ObjectNotFoundException(String.format("Genre id %d not found", id));
        }
    }

    @Override
    public List<Genre> getGenresList() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    }
}

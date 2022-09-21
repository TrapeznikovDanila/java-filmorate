package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmGenreDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmGenresDaoImpl implements FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    public FilmGenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> getFilmGenres(long id) {
        List<Integer> genresList = new ArrayList<>();
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select * from film_genre where film_id = ?", id);
        while (genresRows.next()) {
            genresList.add(genresRows.getInt("genre_id"));
        }
        return genresList;
    }

    @Override
    public void setFilmsGenres(long filmId, List<Integer> genresList) {
        String sqlQuery1 = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery1, filmId);
        String sqlQuery2 = "insert into film_genre(film_id, genre_id) values (?, ?)";

        jdbcTemplate.batchUpdate(sqlQuery2, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genresList.get(i));
            }
            @Override
            public int getBatchSize() {
                return genresList.size();
            }
                });
//        Set<Integer> sortedGenresList = new HashSet<>(genresList);
//
//        for (Integer genreId : sortedGenresList) {
//            jdbcTemplate.update(sqlQuery2,
//                    filmId,
//                    genreId);
//        }
    }
}

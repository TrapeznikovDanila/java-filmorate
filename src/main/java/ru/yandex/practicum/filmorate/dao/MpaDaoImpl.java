package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.dao.interfaces.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private static final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from films_mpa where mpa_id = ?",
                id);
        if (mpaRows.next()) {
            return Mpa.builder()
                            .id(mpaRows.getInt("mpa_id"))
                                    .name(mpaRows.getString("mpa_name"))
                                            .build();
        } else {
            log.info("Mpa id %d not found", id);
            throw new ObjectNotFoundException(String.format("Mpa id %d not found", id));
        }
    }

    @Override
    public List<Mpa> getMpaList() {
        String sqlQuery = "select * from films_mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                        .id(resultSet.getInt("mpa_id"))
                                .name(resultSet.getString("mpa_name"))
                                        .build();
    }
}

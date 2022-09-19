package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTests {
    private final GenreDaoImpl genreDao;

    @Test
    public void testGetGenre() {
        Genre genre = genreDao.getGenre(1);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testGetGenreList() {
        List<Genre> genreList = genreDao.getGenresList();
        assertThat(genreList).hasSize(6);
    }
}

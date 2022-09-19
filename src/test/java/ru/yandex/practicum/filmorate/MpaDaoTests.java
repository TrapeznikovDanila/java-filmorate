package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTests {
    private final MpaDaoImpl mpaDao;

    @Test
    public void testGetMpa() {
        Mpa mpa = mpaDao.getMpa(1);
        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testGetMpaList() {
        List<Mpa> mpaList = mpaDao.getMpaList();
        assertThat(mpaList).hasSize(5);
    }
}

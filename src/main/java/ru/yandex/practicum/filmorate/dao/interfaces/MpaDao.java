package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {

    Mpa getMpa(int id);

    List<Mpa> getMpaList();
}

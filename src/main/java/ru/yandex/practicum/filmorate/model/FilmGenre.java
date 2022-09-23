package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FilmGenre {
    @NotNull
    private int film_id;
    @NotNull
    private int genre_id;
}

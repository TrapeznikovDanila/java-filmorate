package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserLikes {
    @NotNull
    private int filmId;
    @NotNull
    private int userId;
}

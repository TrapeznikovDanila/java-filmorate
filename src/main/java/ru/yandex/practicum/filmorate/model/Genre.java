package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Genre {
    @NotNull
    private int id;
    @Size(max = 50)
    private String name;
}

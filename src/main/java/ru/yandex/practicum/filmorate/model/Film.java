package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}

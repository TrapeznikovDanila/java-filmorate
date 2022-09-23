package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
public class Mpa {
    @NotNull
    private int id;
    @Size(max = 50)
    private String name;
}

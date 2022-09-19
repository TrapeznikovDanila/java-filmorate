package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Friend {
    @NotNull
    private int userId;
    @NotNull
    private int friendId;
    @Size(max = 20)
    private String friendStatus;
}

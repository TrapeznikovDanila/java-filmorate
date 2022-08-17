package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public abstract class Controller<T> {

    @GetMapping
    public abstract List<T> getAll();

    @PostMapping
    public abstract T addObject(@Valid @RequestBody T object);

    @PutMapping
    public abstract T updateObject(@Valid @RequestBody T object);
}

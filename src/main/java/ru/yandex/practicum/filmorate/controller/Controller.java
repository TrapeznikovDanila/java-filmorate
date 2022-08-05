package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class Controller<T> {
    protected HashMap<Integer, T> objects = new HashMap<>();

    @GetMapping
    public List<T> getAll() {
        List<T> objectList = new ArrayList<>();
        for (T object : objects.values()) {
            objectList.add(object);
        }
        return objectList;
    }

    @PostMapping
    public T addObject(@Valid @RequestBody T object) {
        try {
            isValid(object);
        } catch (ValidationException e) {
            throw e;
        }
        return object;
    }

    @PutMapping
    public T updateObject(@Valid @RequestBody T object) {
        try {
            isValid(object);
        } catch (ValidationException e) {
            throw e;
        }
        return object;
    }

    public void isValid(T object) {
    }
}

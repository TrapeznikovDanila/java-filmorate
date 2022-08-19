package ru.yandex.practicum.filmorate.controller;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(final String message) {
        super(message);
    }
}

package ru.yandex.practicum.filmorate.controller;

public class ObjectConflictException extends RuntimeException {
    public ObjectConflictException(final String message) {
        super(message);
    }
}


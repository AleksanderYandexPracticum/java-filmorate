package ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    public Film add(Film film);

    public Film delete(Film film);

    public Film update(Film film);

    public Map<Integer, Film> getAll();
}

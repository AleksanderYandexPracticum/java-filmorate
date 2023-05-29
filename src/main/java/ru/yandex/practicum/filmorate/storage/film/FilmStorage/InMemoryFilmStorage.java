package ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Integer, Film> listFilms = new HashMap<>();
    private int id = 0;

    public HashMap<Integer, Film> getListFilms() {
        return listFilms;
    }

    public int getId() {
        return id;
    }

    @Override
    public Film add(Film film) {
        film.setId(++id);
        listFilms.put(id, film);
        return film;
    }

    @Override
    public Film delete(Film film) {
        listFilms.remove(film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        listFilms.put(film.getId(), film);
        return null;
    }

    @Override
    public Map<Integer, Film> getAll() {
        return listFilms;
    }
}

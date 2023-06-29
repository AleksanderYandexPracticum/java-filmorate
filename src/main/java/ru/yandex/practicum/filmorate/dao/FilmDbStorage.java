package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.Map;

@Component("filmDao")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public HashMap<Integer, Film> getListFilms() {
        return null;//listFilms; //взять из базы
    }
    @Override
    public Film add(Film user) {
        return null;
    }

    @Override
    public Film delete(Film user) {
        return null;
    }

    @Override
    public Film update(Film user) {
        return null;
    }

    @Override
    public Map<Integer, Film> getAll() {
        return null;
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {
    private HashMap<Integer, Film> listFilm = new HashMap<>();
    private int id = 0;

    @PostMapping
    public Film addFilm(HttpServletRequest request, @RequestBody Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Название фильма не может быть пустым",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Максимальная длина описания должна быть — 200 символов",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Максимальная длина описания должна быть — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Дата релиза должна быть — не раньше 28 декабря 1895 года",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Дата релиза должна быть — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Продолжительность фильма должна быть положительной",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        film.setId(++id);
        listFilm.put(id, film);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return film;
    }

    @PutMapping
    public Film updateFilm(HttpServletRequest request, @RequestBody Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Название фильма не может быть пустым",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Максимальная длина описания должна быть — 200 символов",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Максимальная длина описания должна быть — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Дата релиза должна быть — не раньше 28 декабря 1895 года",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Дата релиза должна быть — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Продолжительность фильма должна быть положительной",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        if (!listFilm.containsKey(film.getId())) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' Нет такого идентификатора",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Нет такого идентификатора");
        }
        listFilm.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return film;
    }

    @GetMapping
    public HashMap<Integer, Film> getAllFilm(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return listFilm;
    }
}

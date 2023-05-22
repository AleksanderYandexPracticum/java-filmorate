package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {
    private HashMap<Integer, Film> listFilm = new HashMap<>();
    private int id = 0;

    @PostMapping
    public Film addFilm(HttpServletRequest request, @RequestBody Film film) {
        validationFilm(film);
        film.setId(++id);
        listFilm.put(id, film);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return film;
    }

    @PutMapping
    public Film updateFilm(HttpServletRequest request, @RequestBody Film film) {
        validationFilm(film);
        validationIdFilm(film);

        listFilm.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilm(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return listFilm.values();
    }

    protected void validationFilm(Film film) throws ValidationException {

        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Максимальная длина описания должна быть — 200 символов");
            throw new ValidationException("Максимальная длина описания должна быть — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Дата релиза должна быть — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должна быть — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    protected void validationIdFilm(Film film) throws ValidationException {
        if (!listFilm.containsKey(film.getId())) {
            log.info("Нет такого идентификатора");
            throw new ValidationException("Нет такого идентификатора");
        }
    }
}

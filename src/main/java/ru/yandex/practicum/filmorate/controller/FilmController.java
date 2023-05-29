package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService.FilmService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(HttpServletRequest request, @RequestBody Film film) {
        filmService.validationFilm(film);
        Film returnFilm = filmService.inMemoryFilmStorage.add(film);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return returnFilm;
    }

    @DeleteMapping
    public Film deleteFilm(HttpServletRequest request, @RequestBody Film film) {
        filmService.validationFilm(film);
        filmService.validationIdFilm(film);

        Film returnFilm = filmService.inMemoryFilmStorage.delete(film);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return returnFilm;
    }

    @PutMapping
    public Film updateFilm(HttpServletRequest request, @RequestBody Film film) {
        filmService.validationFilm(film);
        filmService.validationIdFilm(film);

        Film returnFilm = filmService.inMemoryFilmStorage.update(film);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return returnFilm;
    }

    @GetMapping
    public List<Film> getAllFilm(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ArrayList<>(filmService.inMemoryFilmStorage.getListFilms().values());
    }

    @GetMapping("/{id}")
    public Film getFilmInService(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteFilm(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getFilmInService(HttpServletRequest request,
                                       @RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmService.getFilms(count);
    }
}

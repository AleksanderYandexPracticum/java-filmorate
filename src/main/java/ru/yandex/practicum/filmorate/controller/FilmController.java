package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDaoService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {

    private final FilmDaoService filmDaoService;

    @Autowired
    public FilmController(@Qualifier("filmDaoService") FilmDaoService filmDaoService) {
        this.filmDaoService = filmDaoService;
    }

    public FilmDaoService getFilmDaoService() {
        return filmDaoService;
    }

    @PostMapping
    public Film addFilm(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationFilm(film);
        Film returnFilm = filmDaoService.addFilm(film);
        return returnFilm;
    }

    @DeleteMapping
    public Film deleteFilm(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationFilm(film);
        filmDaoService.validationIdFilm(film);
        Film returnFilm = filmDaoService.deleteFilm(film);
        return returnFilm;
    }

    @PutMapping
    public Film updateFilm(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationFilm(film);
        filmDaoService.validationIdFilm(film);
        Film returnFilm = filmDaoService.updateFilm(film);
        return returnFilm;
    }

    @GetMapping
    public List<Film> getAllFilm(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ArrayList<>(filmDaoService.getAllFilm());
    }

    @GetMapping("/{id}")
    public Film getFilmById(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationIdFilm(id);
        return filmDaoService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationIdFilm(id);
        filmDaoService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteFilm(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationIdFilm(id);
        filmDaoService.validationIdFilm(userId);
        filmDaoService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(HttpServletRequest request,
                                      @RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getFilms(count);
    }
}

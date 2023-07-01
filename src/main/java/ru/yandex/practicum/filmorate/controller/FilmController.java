package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDaoService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController()
@RequestMapping()
public class FilmController {

    private final FilmDaoService filmDaoService;

    @Autowired
    public FilmController(@Qualifier("filmDaoService") FilmDaoService filmDaoService) {
        this.filmDaoService = filmDaoService;
    }

    public FilmDaoService getFilmDaoService() {
        return filmDaoService;
    }

    @PostMapping("/films")
    public Film addFilm(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationFilm(film);
        Film returnFilm = filmDaoService.addFilm(film);
        return returnFilm;
    }

    @DeleteMapping("/films")
    public Film deleteFilm(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationFilm(film);
        filmDaoService.validationIdFilm(film);
        Film returnFilm = filmDaoService.deleteFilm(film);
        return returnFilm;
    }

    @PutMapping("/films")
    public Film updateFilm(HttpServletRequest request, @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationFilm(film);
        filmDaoService.validationIdFilm(film);
        Film returnFilm = filmDaoService.updateFilm(film);
        return returnFilm;
    }

    @GetMapping("/films")
    public List<Film> getAllFilm(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return new ArrayList<>(filmDaoService.getAllFilm());
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationIdFilm(id);
        return filmDaoService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationIdFilm(id);
        filmDaoService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteFilm(HttpServletRequest request, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        filmDaoService.validationIdFilm(id);
        filmDaoService.validationIdFilm(userId);
        filmDaoService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(HttpServletRequest request,
                                      @RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getFilms(count);
    }

    @GetMapping("/mpa/{id}")
    public LinkedHashMap<String, Object> getMpaById(HttpServletRequest request, @PathVariable("id") Integer id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getMpa(id);
    }

    @GetMapping("/mpa")
    public List<LinkedHashMap<String, Object>> getAllMpa(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getAllMpa();
    }

    @GetMapping("/genres/{id}")
    public LinkedHashMap<String, Object> getGenresById(HttpServletRequest request, @PathVariable("id") Integer id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getGenresById(id);
    }

    @GetMapping("/genres")
    public LinkedHashSet<LinkedHashMap<String, Object>> getAllGenres(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getAllGenres();
    }

}

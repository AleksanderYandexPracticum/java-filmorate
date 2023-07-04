package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmDaoService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;

@Slf4j
@RestController()
@RequestMapping()
public class GenresController {

    private final FilmDaoService filmDaoService;

    @Autowired
    public GenresController(@Qualifier("filmDaoService") FilmDaoService filmDaoService) {
        this.filmDaoService = filmDaoService;
    }

    public FilmDaoService getFilmDaoService() {
        return filmDaoService;
    }

    @GetMapping("/genres/{id}")
    public Genre getGenresById(HttpServletRequest request, @PathVariable("id") Integer id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getGenresById(id);
    }

    @GetMapping("/genres")
    public LinkedHashSet<Genre> getAllGenres(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return filmDaoService.getAllGenres();
    }
}
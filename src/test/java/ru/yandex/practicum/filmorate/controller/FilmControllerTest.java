package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

//    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
//    FilmService filmService = new FilmService(inMemoryFilmStorage);
//    FilmController filmController = new FilmController(filmService);
//
//    @DisplayName("Проверка")
//    @Test
//    void addFilm() {
//
//        Film film = new Film();
//        film.setName("");
//        film.setDescription("adipisicing");
//        film.setReleaseDate(LocalDate.of(1967, 03, 25));
//        film.setDuration(100);
//
//        final ValidationException exception = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film));
//        assertEquals("Название фильма не может быть пустым", exception.getMessage());
//
//        Film film1 = new Film();
//        film1.setName("nisi eiusmod");
//        film1.setDescription("f".repeat(201));
//        film1.setReleaseDate(LocalDate.of(1967, 03, 25));
//        film1.setDuration(100);
//        final ValidationException exception1 = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film1));
//        assertEquals("Максимальная длина описания должна быть — 200 символов", exception1.getMessage());
//
//        Film film2 = new Film();
//        film2.setName("nisi eiusmod");
//        film2.setDescription("adipisicing");
//        film2.setReleaseDate(LocalDate.of(1895, 12, 27));
//        film2.setDuration(100);
//        final ValidationException exception2 = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film2));
//        assertEquals("Дата релиза должна быть — не раньше 28 декабря 1895 года", exception2.getMessage());
//
//
//        Film film3 = new Film();
//        film3.setName("nisi eiusmod");
//        film3.setDescription("adipisicing");
//        film3.setReleaseDate(LocalDate.of(1967, 03, 25));
//        film3.setDuration(0);
//        final ValidationException exception3 = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film3));
//        assertEquals("Продолжительность фильма должна быть положительной", exception3.getMessage());
//    }
//
//    @DisplayName("Проверка")
//    @Test
//    void updateFilm() {
//        Film film = new Film();
//        film.setName("");
//        film.setDescription("adipisicing");
//        film.setReleaseDate(LocalDate.of(1967, 03, 25));
//        film.setDuration(100);
//        final ValidationException exception = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film));
//        assertEquals("Название фильма не может быть пустым", exception.getMessage());
//
//        Film film1 = new Film();
//        film1.setName("nisi eiusmod");
//        film1.setDescription("f".repeat(201));
//        film1.setReleaseDate(LocalDate.of(1967, 03, 25));
//        film1.setDuration(100);
//        final ValidationException exception1 = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film1));
//        assertEquals("Максимальная длина описания должна быть — 200 символов", exception1.getMessage());
//
//        Film film2 = new Film();
//        film2.setName("nisi eiusmod");
//        film2.setDescription("adipisicing");
//        film2.setReleaseDate(LocalDate.of(1895, 12, 27));
//        film2.setDuration(100);
//        final ValidationException exception2 = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film2));
//        assertEquals("Дата релиза должна быть — не раньше 28 декабря 1895 года", exception2.getMessage());
//
//
//        Film film3 = new Film();
//        film3.setName("nisi eiusmod");
//        film3.setDescription("adipisicing");
//        film3.setReleaseDate(LocalDate.of(1967, 03, 25));
//        film3.setDuration(0);
//        final ValidationException exception3 = assertThrows(ValidationException.class,
//                () -> filmController.getFilmService().validationFilm(film3));
//        assertEquals("Продолжительность фильма должна быть положительной", exception3.getMessage());
//
//        Film film4 = new Film();
//        film4.setId(1);
//        film4.setName("nisi eiusmod");
//        film4.setDescription("adipisicing");
//        film4.setReleaseDate(LocalDate.of(1967, 03, 25));
//        film4.setDuration(0);
//        final NotFoundException exception4 = assertThrows(NotFoundException.class,
//                () -> filmController.getFilmService().validationIdFilm(film4));
//        assertEquals("Нет такого идентификатора № 1", exception4.getMessage());
//    }
}
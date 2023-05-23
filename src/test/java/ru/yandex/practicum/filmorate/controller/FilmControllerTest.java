package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController = new FilmController();

    @Test
    void addFilm() {

        Film film = new Film();
        film.setName("");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());

        Film film1 = new Film();
        film1.setName("nisi eiusmod");
        film1.setDescription("f".repeat(201));
        film1.setReleaseDate(LocalDate.of(1967, 03, 25));
        film1.setDuration(100);
        final ValidationException exception1 = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film1));
        assertEquals("Максимальная длина описания должна быть — 200 символов", exception1.getMessage());

        Film film2 = new Film();
        film2.setName("nisi eiusmod");
        film2.setDescription("adipisicing");
        film2.setReleaseDate(LocalDate.of(1895, 12, 27));
        film2.setDuration(100);
        final ValidationException exception2 = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film2));
        assertEquals("Дата релиза должна быть — не раньше 28 декабря 1895 года", exception2.getMessage());


        Film film3 = new Film();
        film3.setName("nisi eiusmod");
        film3.setDescription("adipisicing");
        film3.setReleaseDate(LocalDate.of(1967, 03, 25));
        film3.setDuration(0);
        final ValidationException exception3 = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film3));
        assertEquals("Продолжительность фильма должна быть положительной", exception3.getMessage());
    }


    @Test
    void updateFilm() {
        Film film = new Film();
        film.setName("");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());

        Film film1 = new Film();
        film1.setName("nisi eiusmod");
        film1.setDescription("f".repeat(201));
        film1.setReleaseDate(LocalDate.of(1967, 03, 25));
        film1.setDuration(100);
        final ValidationException exception1 = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film1));
        assertEquals("Максимальная длина описания должна быть — 200 символов", exception1.getMessage());

        Film film2 = new Film();
        film2.setName("nisi eiusmod");
        film2.setDescription("adipisicing");
        film2.setReleaseDate(LocalDate.of(1895, 12, 27));
        film2.setDuration(100);
        final ValidationException exception2 = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film2));
        assertEquals("Дата релиза должна быть — не раньше 28 декабря 1895 года", exception2.getMessage());


        Film film3 = new Film();
        film3.setName("nisi eiusmod");
        film3.setDescription("adipisicing");
        film3.setReleaseDate(LocalDate.of(1967, 03, 25));
        film3.setDuration(0);
        final ValidationException exception3 = assertThrows(ValidationException.class,
                () -> filmController.validationFilm(film3));
        assertEquals("Продолжительность фильма должна быть положительной", exception3.getMessage());

        Film film4 = new Film();
        film4.setId(1);
        film4.setName("nisi eiusmod");
        film4.setDescription("adipisicing");
        film4.setReleaseDate(LocalDate.of(1967, 03, 25));
        film4.setDuration(0);
        final ValidationException exception4 = assertThrows(ValidationException.class,
                () -> filmController.validationIdFilm(film4));
        assertEquals("Нет такого идентификатора", exception4.getMessage());
    }
}
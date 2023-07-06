package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("filmDaoService")
public class FilmDaoService {

    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmDaoService(@Qualifier("filmDao") FilmStorage filmStorage) {
        this.filmDbStorage = (FilmDbStorage) filmStorage;
    }

    public void validationFilm(Film film) {

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

    public void validationIdFilm(Film film) {
        if (!filmDbStorage.getListFilms().containsKey(film.getId())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", film.getId()));
        }
    }

    public void validationIdFilm(Long id) {
        if (!filmDbStorage.getListFilms().containsKey(id.intValue())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", id));
        }
    }

    public Film getFilm(Long id) {  //получение данных о фильме по иго уникальному идентификатору
        return filmDbStorage.getListFilms().get(id.intValue());
    }

    public void addLike(Long id, Long userId) {  //пользователь ставит лайк фильму
        filmDbStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {  //пользователь удаляет лайк
        filmDbStorage.deleteLike(id, userId);
    }

    public List<Film> getFilms(int count) {  //возвращает список из первых count фильмов по количеству лайков.
        // Если значение параметра count не задано, верните первые 10.
        List<Film> films = new ArrayList<>(filmDbStorage.getListFilms().values())
                .stream()
                .sorted((p0, p1) -> ((Integer) p1.getUserIdsWhoLiked().size()).compareTo(p0.getUserIdsWhoLiked().size()))
                .limit(count)
                .collect(Collectors.toList());
        return films;
    }

    public Film addFilm(Film film) {
        return filmDbStorage.add(film);
    }

    public Film deleteFilm(Film film) {
        return filmDbStorage.delete(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.update(film);
    }

    public Collection<Film> getAllFilm() {
        return filmDbStorage.getListFilms().values();
    }

    public Mpa getMpa(Integer id) { // Получение mpa по id
        return filmDbStorage.getMpa(id);
    }

    public List<Mpa> getAllMpa() { // Получение всех mpa
        return filmDbStorage.getAllMpa();
    }

    public Genre getGenresById(Integer id) { // Получение genre по id
        return filmDbStorage.getGenresById(id);
    }

    public LinkedHashSet<Genre> getAllGenres() { // Получение всех genre
        return filmDbStorage.getAllGenres();
    }
}
package ru.yandex.practicum.filmorate.service.film.FilmService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.inMemoryFilmStorage = (InMemoryFilmStorage) filmStorage;
    }

    public InMemoryFilmStorage getInMemoryFilmStorage() {
        return inMemoryFilmStorage;
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
        if (!inMemoryFilmStorage.getListFilms().containsKey(film.getId())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", film.getId()));
        }
    }

    public void validationIdFilm(Long id) {
        if (!inMemoryFilmStorage.getListFilms().containsKey(id.intValue())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", id));
        }
    }

    public Film getFilm(Long id) {  //получение данных о фильме по иго уникальному идентификатору
        return inMemoryFilmStorage.getListFilms().get(id.intValue());
    }

    public void addLike(Long id, Long userId) {  //пользователь ставит лайк фильму
        inMemoryFilmStorage.getListFilms().get(id.intValue()).getUserIdsWhoLiked().add(userId);
    }

    public void deleteLike(Long id, Long userId) {  //пользователь удаляет лайк
        inMemoryFilmStorage.getListFilms().get(id.intValue()).getUserIdsWhoLiked().remove(userId);
    }

    public List<Film> getFilms(int count) {  //возвращает список из первых count фильмов по количеству лайков.
        // Если значение параметра count не задано, верните первые 10.
        List<Film> films = new ArrayList<>(inMemoryFilmStorage.getListFilms().values())
                .stream()
                .sorted((p0, p1) -> ((Integer) p1.getUserIdsWhoLiked().size()).compareTo(p0.getUserIdsWhoLiked().size()))
                .limit(count)
                .collect(Collectors.toList());
        return films;
    }
}

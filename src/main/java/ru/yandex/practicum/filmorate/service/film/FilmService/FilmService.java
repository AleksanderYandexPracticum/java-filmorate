package ru.yandex.practicum.filmorate.service.film.FilmService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    public final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.inMemoryFilmStorage = (InMemoryFilmStorage) filmStorage;
    }


    public void validationFilm(Film film) throws ValidationException {

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

    public void validationIdFilm(Film film) throws ValidationException {
        if (!inMemoryFilmStorage.getListFilms().containsKey(film.getId())) {
            log.info("Нет такого идентификатора");
            throw new ValidationException("Нет такого идентификатора");
        }
    }

    public void validationIdFilm(Long id) throws ValidationException {
        if (!inMemoryFilmStorage.getListFilms().containsKey(id)) {
            log.info("Нет такого идентификатора");
            throw new ValidationException(String.format("Нет такого идентификатора № s%", id));
        }
    }

    public Film getFilm(Long id) {  //получение данных о фильме по иго уникальному идентификатору
        validationIdFilm(id);
        return inMemoryFilmStorage.getListFilms().get(id);
    }

    public void addLike(Long id, Long userId) {  //пользователь ставит лайк фильму
        validationIdFilm(id);
        inMemoryFilmStorage.getListFilms().get(id).getLikes().add(userId);
    }

    public void deleteLike(Long id, Long userId) {  //пользователь удаляет лайк
        validationIdFilm(id);
        inMemoryFilmStorage.getListFilms().get(id).getLikes().remove(userId);
    }

    public List<Film> getFilms(int count) {  //возвращает список из первых count фильмов по количеству лайков.
        // Если значение параметра count не задано, верните первые 10.
        if (inMemoryFilmStorage.getListFilms() == null || inMemoryFilmStorage.getListFilms().size() == 0) {
            throw new ValidationException("Фильмов нет");
        }
//        if (count == 0) {
//            count = 10;
//        }
        List<Film> films = new ArrayList<>(inMemoryFilmStorage.getListFilms().values()).
                stream().
                sorted(Comparator.comparing(p0 -> ((Integer) p0.getLikes().size()))).
                limit(count).
                collect(Collectors.toList());
        return films;
    }
}

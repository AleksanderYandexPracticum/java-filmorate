package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("filmDao")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage likeDbStorage;


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage,
                         GenreDbStorage genreDbStorage, LikeDbStorage likeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.likeDbStorage = likeDbStorage;
    }

    public HashMap<Integer, Film> getListFilms() {
        String sqlQuery = "SELECT * FROM FILMS";
        Collection<Film> filmList = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
        HashMap<Integer, Film> filmMap = new HashMap<>();
        for (Film film : filmList) {
            filmMap.put(film.getId(), film);
        }
        return filmMap;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        // Получаем дату и конвертируем её из sql.Date в time.LocalDate
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        Long duration = rs.getLong("duration");

        LinkedHashSet<Long> userIdsWhoLiked = makeLike(id);
        LinkedHashSet<Genre> genres = makeGenre(id);

        Integer mpaId = rs.getInt("mpa_id"); // устанавливаем рейтинг
        Mpa mpa = makeMpa(mpaId, id);

        return new Film(id, name, description, releaseDate, duration, userIdsWhoLiked, genres, mpa);
    }

    private LinkedHashSet<Long> makeLike(Integer id) {
        return likeDbStorage.makeLike(id);
    }

    private LinkedHashSet<Genre> makeGenre(Integer id) {
        return genreDbStorage.makeGenre(id);
    }

    private Mpa makeMpa(Integer mpaId, Integer id) {
        return mpaDbStorage.makeMpa(mpaId, id);
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        String sqlQuery = "UPDATE FILMS SET MPA_ID=? WHERE FILM_ID=?";

        if (film.getMpa() != null) {
            jdbcTemplate.update(sqlQuery, film.getMpa().getId(), id);
            film.setMpa(makeMpa(film.getMpa().getId(), id));
        }
        sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?,?)";

        if (film.getGenres() != null && film.getGenres().size() != 0) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, id, genre.getId());
            }
        }
        film.setId(id);
        return film;
    }

    @Override
    public Film delete(Film film) {
        int id = film.getId();
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        return film;
    }

    @Override
    public Film update(Film film) {
        int id = film.getId();
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";// Удаляю старые записи о жанрах
        jdbcTemplate.update(sqlQuery, id);
        sqlQuery = "DELETE FROM USERWHOLIKED WHERE FILM_ID=?";// Удаляю старые записи о тех кто лайкнул
        jdbcTemplate.update(sqlQuery, id);

        LinkedHashSet<Genre> genres = film.getGenres(); // Заполняю таблицу FILM_GENRE записями о жанрах
        if (genres != null && genres.size() > 0) {
            for (Genre genre : genres) {
                sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES  (?,?)";

                jdbcTemplate.update(sqlQuery, id, genre.getId());
            }
        }

        LinkedHashSet<Long> userIdsWhoLiked = film.getUserIdsWhoLiked(); // Заполняю таблицу USERWHOLIKED записями о лайках
        if (userIdsWhoLiked != null) {
            for (Long userId : userIdsWhoLiked) {
                sqlQuery = "INSERT INTO USERWHOLIKED (FILM_ID, USER_ID) VALUES  (?,?)";
                jdbcTemplate.update(sqlQuery, id, userId);
            }
        }
        sqlQuery = "UPDATE FILMS SET FILM_ID = ?, NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, " +
                "DURATION=? , MPA_ID=? WHERE FILM_ID = ?";

        jdbcTemplate.update(sqlQuery, id, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), id);

        return film;
    }

    @Override
    public Map<Integer, Film> getAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        Collection<Film> filmList = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
        HashMap<Integer, Film> filmMap = new HashMap<>();
        for (Film film : filmList) {
            filmMap.put(film.getId(), film);
        }
        return filmMap;
    }

    public void addLike(Long id, Long userId) {
        likeDbStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        likeDbStorage.deleteLike(id, userId);
    }

    public Mpa getMpa(Integer id) {  // Возвращаю название MPA по id
        return mpaDbStorage.getMpa(id);
    }

    public List<Mpa> getAllMpa() {  // Возвращаю все название MPA
        return mpaDbStorage.getAllMpa();
    }

    public Genre getGenresById(Integer id) {  // Возвращаю название жанра по id
        return genreDbStorage.getGenresById(id);
    }

    public LinkedHashSet<Genre> getAllGenres() {  // Возвращаю все жанры
        return genreDbStorage.getAllGenres();
    }
}
package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();

        String sqlQuery = "SELECT u.USER_ID FROM FILMS f " +
                "JOIN USERWHOLIKED u ON f.FILM_ID=u.FILM_ID " +
                "WHERE f.FILM_ID =?";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (likeRows.next()) {
            Long userId = likeRows.getLong("user_id");
            userIdsWhoLiked.add(userId);
        }
        return userIdsWhoLiked;
    }

    private LinkedHashSet<Genre> makeGenre(Integer id) {
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();

        String sqlQuery = "SELECT g.GENRE_ID, g.NAME FROM FILM_GENRE fg " +      // устанавливаем жанры
                "JOIN GENRE g ON fg.GENRE_ID=g.GENRE_ID " +
                "WHERE fg.FILM_ID =?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        while (genreRows.next()) {
            Integer genreId = genreRows.getInt("genre_id");
            String nameGenre = genreRows.getString("name");
            Genre genre = new Genre(genreId, nameGenre);
            genres.add(genre);
        }
        return genres;
    }

    private Mpa makeMpa(Integer mpaId, Integer id) {

        String sqlQuery = "SELECT m.NAME FROM FILMS f JOIN MPA m ON m.MPA_ID=f.MPA_ID WHERE f.FILM_ID =?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        String nameMpa = null;
        if (mpaRows.next()) {
            nameMpa = mpaRows.getString("name");
        }
        return new Mpa(mpaId, nameMpa);
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
        deleteLike(id, userId);
        String sqlQuery = "INSERT INTO USERWHOLIKED (FILM_ID, USER_ID) VALUES  (?,?)";  // вставляю лайк
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        String sqlQuery = "DELETE FROM USERWHOLIKED WHERE FILM_ID=? AND USER_ID=?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public Mpa getMpa(Integer id) {  // Возвращаю название MPA по id
        String sqlQuery = "SELECT MPA_ID, NAME FROM MPA WHERE MPA_ID =?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRows.next()) {
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", id));
        }

        String name = mpaRows.getString("name");
        return new Mpa(id, name);
    }

    public List<Mpa> getAllMpa() {  // Возвращаю все название MPA
        List<Mpa> allMpa = new LinkedList<>();
        String sqlQuery = "SELECT * FROM MPA";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery);
        while (mpaRows.next()) {
            Integer id = mpaRows.getInt("mpa_id");
            String name = mpaRows.getString("name");
            allMpa.add(new Mpa(id, name));
        }
        return allMpa;
    }

    public Genre getGenresById(Integer id) {  // Возвращаю название жанра по id
        String sqlQuery = "SELECT GENRE_ID, NAME FROM GENRE WHERE GENRE_ID =?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!genreRows.next()) {
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", id));
        }
        String name = genreRows.getString("name");
        return new Genre(id, name);
    }

    public LinkedHashSet<Genre> getAllGenres() {  // Возвращаю все жанры
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        String sqlQuery = "SELECT * FROM GENRE";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery);
        while (genreRows.next()) {
            Integer id = genreRows.getInt("genre_id");
            String name = genreRows.getString("name");
            genres.add(new Genre(id, name));
        }
        return genres;
    }
}
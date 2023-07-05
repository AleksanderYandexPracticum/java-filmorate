package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LinkedHashSet<Genre> makeGenre(Integer id) {
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

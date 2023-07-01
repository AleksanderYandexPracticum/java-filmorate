package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
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

        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();
        LinkedHashSet<HashMap<String, Object>> genres = new LinkedHashSet<>();
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();
//

        Integer mpaId = rs.getInt("MPA_ID"); // устанавливаем рейтинг
        mpa.put("id", mpaId);
        String sqlQuery = "SELECT m.NAME FROM FILMS f JOIN MPA m ON m.MPA_ID=f.MPA_ID WHERE f.FILM_ID =?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            String nameMpa = filmRows.getString("NAME");
            mpa.put("name", nameMpa);
        }

        sqlQuery = "SELECT u.USER_ID FROM FILMS f " +
                "JOIN USERWHOLIKED u ON f.FILM_ID=u.FILM_ID " +
                "WHERE f.FILM_ID =?";
        filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (filmRows.next()) {
            Long userId = filmRows.getLong("USER_ID");
            userIdsWhoLiked.add(userId);
        }

        sqlQuery = "SELECT fg.GENRE_ID FROM FILM_GENRE fg " +       // устанавливаем жанры
                "JOIN GENRE g ON fg.GENRE_ID=g.GENRE_ID " +
                "WHERE fg.FILM_ID =?";
        filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);


        while (filmRows.next()) {
            Integer genreId = filmRows.getInt("GENRE_ID");
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", genreId);
            genres.add(map);
            String nameGenre = filmRows.getString("NAME");
            map = new HashMap<>();
            map.put("name", nameGenre);
            genres.add(map);
        }

        return new Film(id, name, description, releaseDate, duration, userIdsWhoLiked, genres, mpa);
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        String sqlQuery = "UPDATE FILMS SET MPA_ID=? WHERE FILM_ID=?";
        ArrayList<Object> listIdMpa = new ArrayList<>(film.getMpa().values());
        jdbcTemplate.update(sqlQuery, listIdMpa.get(0), id);
        if (film.getGenres() == null) {   // Установка 0 если жанра нет
            LinkedHashSet<HashMap<String, Object>> genres = new LinkedHashSet<>();
             film.setGenres(genres);
        }
        if (film.getUserIdsWhoLiked() == null) {   // Установка 0 если жанра нет
            LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();
            film.setUserIdsWhoLiked(userIdsWhoLiked);
        }
        film.setId(id);
        return film;
    }

    @Override
    public Film delete(Film film) {
        int id = film.getId();
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
//        deleteGenre(film.getGenres());    //УДАЛЯЮ ЗАПИСЬ ИЗ ТАБЛИЦЫ GENRE
        return film;
    }
//    private void deleteGenre(Set <Integer> genreId) {
//        for (Integer id : genreId) {
//            String sqlQuery = "DELETE FROM GENRE g WHERE GENRE_ID=?";
//            jdbcTemplate.update(sqlQuery, id);
//        }
//    }

    @Override
    public Film update(Film film) {
        int id = film.getId();
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";// Удаляю старые записи о жанрах
        jdbcTemplate.update(sqlQuery, id);
        sqlQuery = "DELETE FROM USERWHOLIKED WHERE FILM_ID=?";// Удаляю старые записи о тех кто лайкнул
        jdbcTemplate.update(sqlQuery, id);

        LinkedHashSet<HashMap<String, Object>> genres = film.getGenres(); // Заполняю таблицу FILM_GENRE записями о жанрах
        if (genres != null && genres.size() > 0) {
            for (HashMap <String, Object> genreId : genres) {
                sqlQuery = "INSERT FILM_GENRE (FILM_ID, GENRE_ID) VALUES  (?,?)";

                jdbcTemplate.update(sqlQuery, id, genreId.get("id"));
            }
        }

        Set<Long> userIdsWhoLiked = film.getUserIdsWhoLiked(); // Заполняю таблицу USERWHOLIKED записями о лайках
        if (userIdsWhoLiked != null) {
            for (Long userId : userIdsWhoLiked) {
                sqlQuery = "INSERT USERWHOLIKED (FILM_ID, USER_ID) VALUES  (?,?)";
                jdbcTemplate.update(sqlQuery, id, userId);
            }
        }
        sqlQuery = "UPDATE FILMS SET FILM_ID = ?, NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, " +
                "DURATION=? , MPA_ID=? WHERE FILM_ID = ?";

        ArrayList<Object> listIdMpa = new ArrayList<>(film.getMpa().values());
        jdbcTemplate.update(sqlQuery, id, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), listIdMpa.get(0), id);

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

//    private Boolean checkLike(Long id, Long userId) { // Проверка наличия лайка
//        String sqlQuery = "SELECT f.FILM_ID FROM FILM f JOIN USERWHOLIKED u ON f.FILM_ID=u.FILM_ID " +
//                "WHERE FILM_ID= ?, USER_ID= ?";            // проверяю наличие лайка пользователя на фильм
//        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id, userId);
//        if (userRows.next()) {
//            return true;
//        }
//        return false;
//    }

    public LinkedHashMap<String, Object> getMpa(Long id){  // Возвращаю название MPA по id
        String sqlQuery = "SELECT MPA_ID FROM MPA WHERE MPA_ID =?";
        SqlRowSet filmRows=jdbcTemplate.queryForRowSet(sqlQuery, id);
        if(!filmRows.next()){
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", id));
        }

        LinkedHashMap<String, Object> mpa= new LinkedHashMap<>();
        sqlQuery = "SELECT NAME FROM MPA WHERE MPA_ID =?";
        filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            String nameMpa = filmRows.getString("NAME");
            mpa.put("id", id);
            mpa.put("name", nameMpa);
        }
        return mpa;
    }

    public List<LinkedHashMap<String, Object>> getAllMpa(){  // Возвращаю все название MPA
        List<LinkedHashMap<String, Object>> allMpa= new LinkedList<>();
        String sqlQuery = "SELECT MPA_ID, NAME FROM MPA";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery);
        while (filmRows.next()) {
            LinkedHashMap<String, Object> mpa= new LinkedHashMap<>();
            Integer id = filmRows.getInt("MPA_ID");
            String nameMpa = filmRows.getString("NAME");
            mpa.put("id", id);
            mpa.put("name", nameMpa);
            allMpa.add(mpa);
        }
        return allMpa;
    }
}
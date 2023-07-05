package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.LinkedList;
import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa makeMpa(Integer mpaId, Integer id) {

        String sqlQuery = "SELECT m.NAME FROM FILMS f JOIN MPA m ON m.MPA_ID=f.MPA_ID WHERE f.FILM_ID =?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        String nameMpa = null;
        if (mpaRows.next()) {
            nameMpa = mpaRows.getString("name");
        }
        return new Mpa(mpaId, nameMpa);
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
}

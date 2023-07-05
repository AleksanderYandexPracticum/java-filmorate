package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LinkedHashSet<Long> makeLike(Integer id) {
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

    public void addLike(Long id, Long userId) {
        deleteLike(id, userId);
        String sqlQuery = "INSERT INTO USERWHOLIKED (FILM_ID, USER_ID) VALUES  (?,?)";  // вставляю лайк
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        String sqlQuery = "DELETE FROM USERWHOLIKED WHERE FILM_ID=? AND USER_ID=?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

}

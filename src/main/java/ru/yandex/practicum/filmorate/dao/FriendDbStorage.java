package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteFriends(int id) {  // Удаляю друзей из таблицы FRIENDSHIPS
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIPS WHERE USER_ID = ?";  // Нахожу всех друзей
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (userRows.next()) {                               // Удаляю друзей у котрого конкретный user
            Integer friendId = userRows.getInt("FRIEND_ID");
            sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlQuery, friendId, id);
        }
        sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID = ?";  // Удаляю у user всех друзей
        jdbcTemplate.update(sqlQuery, id);
    }

    public void addFriend(Long id, Long friendId) {
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";// получаю друга usera
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id, friendId);
        if (userRows.next()) { // Если друг уже есть, то просто выйти из метода
            return;
        }
        sqlQuery = "SELECT CONFIRMATION FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
        userRows = jdbcTemplate.queryForRowSet(sqlQuery, friendId, id);
        Boolean confirm = false;
        if (userRows.next()) { //
            confirm = true;
            sqlQuery = "UPDATE FRIENDSHIPS SET CONFIRMATION=? WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sqlQuery, confirm, friendId, id);
        }

        sqlQuery = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, CONFIRMATION) VALUES  (?,?,?)"; // добавляю friendshipsId и id в FRIENDS
        jdbcTemplate.update(sqlQuery, id, friendId, confirm);
    }

    public void deleteFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID=? AND FRIEND_ID=?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        jdbcTemplate.update(sqlQuery, friendId, id);
    }
}

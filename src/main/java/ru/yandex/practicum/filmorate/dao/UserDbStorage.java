package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("userDao")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public HashMap<Integer, User> getListUsers() {
        String sql = "SELECT * FROM USERS";

        Collection<User> userList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

        HashMap<Integer, User> userMap = new HashMap<>();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
        return userMap;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        // Получаем дату и конвертируем её из sql.Date в time.LocalDate
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();

        String sqlFriends = "SELECT fs.FRIEND_ID, fs.CONFIRMATION FROM FRIENDSHIPS fs WHERE fs.USER_ID =?";

        //Set<Long> friends = new HashSet<>();
        Map<Long, Boolean> friendships = new HashMap<>();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFriends, id);
        while (userRows.next()) {
            Long friend = userRows.getLong("FRIEND_ID");
            friendships.put(friend, userRows.getBoolean("CONFIRMATION"));
        }
        return new User(id, email, login, name, birthday, friendships);
    }

    private void deleteFriends(int id) {  // Удаляю друзей из таблицы FRIENDSHIPS
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

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        int id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User delete(User user) {
        int id = user.getId();
        deleteFriends(id);

        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        return user;
    }

    @Override
    public User update(User user) {
        Integer id = user.getId();
        deleteFriends(id);       // Удалю всех друзей у user

        if (user.getFriendships() != null && user.getFriendships().size() > 0) { // Добавляю друзей к user
            for (Map.Entry<Long, Boolean> friend : user.getFriendships().entrySet()) {
                String sqlUpdate = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, CONFIRMATION) VALUES (?, ?, ?)";
                jdbcTemplate.update(sqlUpdate, id, friend.getKey(), friend.getValue());
                if (friend.getValue() == true) {
                    sqlUpdate = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, CONFIRMATION) VALUES (?, ?, ?)";
                    jdbcTemplate.update(sqlUpdate, friend.getKey(), id, friend.getValue());
                }
            }
        }
        String sqlQuery = "UPDATE USERS SET USER_ID = ?, EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY=? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), id);
        return user;
    }

    @Override
    public Map<Integer, User> getAll() {
        String sql = "SELECT * FROM USERS";
        Collection<User> userList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

        HashMap<Integer, User> userMap = new HashMap<>();
        for (User user : userList) {
            userMap.put(user.getId(), user);
        }
        return userMap;
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

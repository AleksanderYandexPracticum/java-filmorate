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

        String sqlFriends = "SELECT fs.FRIEND_ID, fs.CONFIRMATION FROM FRIENDS f " +
                "JOIN FRIENDSHIPS fs ON f.FRIENDSHIPS_ID = fs.FRIENDSHIPS_ID WHERE f.USER_ID =?";

        Set<Long> friends = new HashSet<>();
        Map<Long, Integer> friendships = new HashMap<>();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFriends, id);
        while (userRows.next()) {
            Long friend = userRows.getLong("FRIEND_ID");
            friends.add(friend);
            friendships.put(friend, userRows.getInt("CONFIRMATION"));
        }
        return new User(id, email, login, name, birthday, friends, friendships);
    }

    private void deleteFriends(int id) {  // Удаляю друзей из таблиц FRIENDS и FRIENDSHIPS
        String sqlQueryDeleteInFriendships = "DELETE FROM FRIENDSHIPS WHERE FRIENDSHIPS_ID IN (" +
                "SELECT f.FRIENDSHIPS_ID FROM FRIENDS f WHERE USER_ID = ?)";
        jdbcTemplate.update(sqlQueryDeleteInFriendships, id);

        String sqlQueryDeleteInFriends = "DELETE FROM FRIENDS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteInFriends, id);
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
        String sqlQueryDeleteInUsers = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteInUsers, id);

        deleteFriends(id);
        return user;
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        if (user.getFriendships() == null || user.getFriendships().size() == 0) {
            deleteFriends(id);
        } else {
            String sqlQueryOldFriends = "SELECT FRIEND_ID FROM FRIENDSHIPS WHERE FRIENDSHIPS_ID IN (" +
                    "SELECT f.FRIENDSHIPS_ID FROM FRIENDS f WHERE f.USER_ID = ?)";
            Set<Integer> oldfriends = new HashSet<>();
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQueryOldFriends, id);
            if (userRows.next()) {
                oldfriends.add(userRows.getInt("FRIEND_ID"));
            }
            for (Integer oldfriend : oldfriends) {
                if (!user.getFriendships().keySet().contains(oldfriend)) {
                    deleteFriends(oldfriend);
                }
            }
        }
        if (user.getFriendships() != null && user.getFriendships().size() > 0) {
            for (Map.Entry<Long, Integer> entry : user.getFriendships().entrySet()) {
                String sqlUpdateFriend = "UPDATE FRIENDSHIPS SET CONFIRMATION = ? WHERE FRIEND_ID = ?";
                jdbcTemplate.update(sqlUpdateFriend, entry.getValue(), entry.getKey());
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
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIPS fs " +
                "WHERE FRIENDSHIPS_ID IN (SELECT f.FRIENDSHIPS_ID FROM FRIENDS f WHERE f.USER_ID = ?) " +
                "AND fs.FRIEND_ID=?"; // получаю спискок друзей друга
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id.intValue(), friendId.intValue());
        if (userRows.next()) {
            return;
        }
        sqlQuery = "UPDATE FRIENDSHIPS fs SET CONFIRMATION=? " +
                "WHERE FRIENDSHIPS_ID IN (SELECT f.FRIENDSHIPS_ID FROM FRIENDS f WHERE f.USER_ID = ?) " +
                "AND fs.CONFIRMATION=0"; // обновляю статус дружбы друга
        int confirm = 0;
        if (jdbcTemplate.update(sqlQuery, 1, friendId.intValue()) > 0) {
            confirm = 1;
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FRIENDSHIPS")
                .usingGeneratedKeyColumns("FRIENDSHIPS_ID");
        Map<String, Integer> params = Map.of("FRIEND_ID", friendId.intValue(), "CONFIRMATION", confirm);
        Number friendshipsId = simpleJdbcInsert.executeAndReturnKey(params);

        sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIENDSHIPS_ID) VALUES  (?,?)"; // добавляю friendshipsId и id в FRIENDS
        jdbcTemplate.update(sqlQuery, id.intValue(), friendshipsId.intValue());
    }

    public void deleteFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIPS fs " +
                "WHERE FRIENDSHIPS_ID IN (SELECT f.FRIENDSHIPS_ID FROM FRIENDS f WHERE f.USER_ID = ?) " +
                "AND fs.FRIEND_ID=?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        jdbcTemplate.update(sqlQuery, friendId, id);
    }
}

package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDaoService;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    private final UserDaoService userDaoService;
    private final UserDbStorage userDbStorage;

    @Test
    public void testFindUserById() {
        Map<Long, Boolean> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friendships);
        userDbStorage.add(user);

        User userFromBd = userDbStorage.getListUsers().get(1);
        assertEquals(1, userFromBd.getId());
        userFromBd.setId(2);
        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userDaoService.validationIdUser(userFromBd));
        exception.getMessage();
        assertEquals("Нет такого идентификатора № 2", exception.getMessage());
    }

    @Test
    public void delete() {
        Map<Long, Boolean> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friendships);
        userDbStorage.add(user);

        User deleteUser = userDbStorage.delete(user);
        assertEquals(1, deleteUser.getId());
    }

    @Test
    public void update() {
        Map<Long, Boolean> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friendships);
        userDbStorage.add(user);

        LocalDate dateUp = LocalDate.parse("1976-09-20");
        User userUp = new User(1, "mail@yandex.ru", "doloreUpdate", "est adipisicing", dateUp, friendships);

        User newUser = userDbStorage.update(userUp);
        assertEquals(1, newUser.getId());
        assertEquals("mail@yandex.ru", newUser.getEmail());
        assertEquals("doloreUpdate", newUser.getLogin());
        assertEquals("est adipisicing", newUser.getName());

    }

    @Test
    public void getAll() {
        Map<Long, Boolean> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friendships);
        userDbStorage.add(user);

        LocalDate otherDate = LocalDate.parse("1976-08-20");
        User otherUser = new User(2, "friend@mail.ru", "friend", "friend adipisicing", otherDate,
                friendships);
        userDbStorage.add(otherUser);

        Map<Integer, User> allUser = userDbStorage.getListUsers();

        assertEquals(1, allUser.get(1).getId());
        assertEquals("mail@mail.ru", allUser.get(1).getEmail());
        assertEquals("dolore", allUser.get(1).getLogin());
        assertEquals("Nick Name", allUser.get(1).getName());
        assertTrue(allUser.containsKey(1));

        assertEquals(2, allUser.get(2).getId());
        assertEquals("friend@mail.ru", allUser.get(2).getEmail());
        assertEquals("friend", allUser.get(2).getLogin());
        assertEquals("friend adipisicing", allUser.get(2).getName());
        assertTrue(allUser.containsKey(2));
    }

    @Test
    public void addFriend() {
        Map<Long, Boolean> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friendships);
        userDbStorage.add(user);

        LocalDate otherDate = LocalDate.parse("1976-08-20");
        User otherUser = new User(2, "friend@mail.ru", "friend", "friend adipisicing",
                otherDate, friendships);
        userDbStorage.add(otherUser); // добавляю в БД друга
        userDbStorage.addFriend(1L, 2L);  // добавляю юзеру  друга

        User userWithFriend = userDbStorage.getListUsers().get(1);

        assertTrue(userWithFriend.getFriendships().containsKey(2L)); // проверяю у юзер дружбу и подтверждение "true-подтвержд"
        assertTrue(userWithFriend.getFriendships().containsValue(false));

        userDbStorage.addFriend(2L, 1L);   // добавляю  другу в друзья юзера
        User friend = userDbStorage.getListUsers().get(2);

        assertTrue(friend.getFriendships().containsKey(1L));// проверяю у друга дружбу и подтверждение "true-подтвержд"
        assertTrue(friend.getFriendships().containsValue(true));

        userWithFriend = userDbStorage.getListUsers().get(1);   // читаю из БД обновленное значения подтверждения дружбы
        assertTrue(userWithFriend.getFriendships().containsValue(true));
    }

    @Test
    public void deleteFriend() {
        Set<Long> friends = new HashSet<>();
        Map<Long, Boolean> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friendships);
        userDbStorage.add(user);

        LocalDate otherDate = LocalDate.parse("1976-08-20");
        User otherUser = new User(2, "friend@mail.ru", "friend", "friend adipisicing",
                otherDate, friendships);
        userDbStorage.add(otherUser); // добавляю в БД друга
        userDbStorage.addFriend(1L, 2L);  // добавляю юзеру друга

        userDbStorage.addFriend(2L, 1L);   // добавляю  другу в друзья юзера

        userDbStorage.deleteFriend(1L, 2L);

        User userWithoutFriend = userDbStorage.getListUsers().get(1);

        assertTrue(userWithoutFriend.getFriendships().size() == 0);   // проверяю у юзер дружбу

        User friend = userDbStorage.getListUsers().get(2);

        assertTrue(friend.getFriendships().size() == 0);   // проверяю у друга дружбу

    }
}
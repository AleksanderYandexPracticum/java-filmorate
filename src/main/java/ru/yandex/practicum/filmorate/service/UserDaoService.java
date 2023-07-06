package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service("userDaoService")
public class UserDaoService {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserDaoService(@Qualifier("userDao") UserStorage userStorage) {
        this.userDbStorage = (UserDbStorage) userStorage;
    }

    public void validationUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public void validationIdUser(User user) {
        if (!userDbStorage.getListUsers().containsKey(user.getId())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", user.getId()));
        }
    }

    public void validationIdUser(Long id) {
        if (!userDbStorage.getListUsers().containsKey(id.intValue())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", id));
        }
    }

    public User getUser(Long id) {  //получение данных о пользователе по иго уникальному идентификатору
        return userDbStorage.getListUsers().get(id.intValue());
    }

    public void addFriend(Long id, Long friendId) {  //добавление в друзья
        userDbStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {  //удаление из друзей
        userDbStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriendsCurrentUser(Long id) { //возвращаем список пользователей, являющихся его друзьями
        List<User> allFriends = new ArrayList<>();
        for (Long friendId : userDbStorage.getListUsers().get(id.intValue()).getFriendships().keySet()) {
            allFriends.add(userDbStorage.getListUsers().get(friendId.intValue()));
        }
        return allFriends;
    }

    public List<User> getCommonFriendsList(Long id, Long otherId) {  //список друзей, общих с другим пользователем
        Set<Long> setId = userDbStorage.getListUsers().get(id.intValue()).getFriendships().keySet();
        Set<Long> setFriendId = userDbStorage.getListUsers().get(otherId.intValue()).getFriendships().keySet();
        setId.retainAll(setFriendId);

        List<User> commonFriends = new ArrayList<>();

        for (Long friendId : setId) {
            commonFriends.add(userDbStorage.getListUsers().get(friendId.intValue()));
        }
        return commonFriends;
    }

    public User addUser(User user) {
        return userDbStorage.add(user);
    }

    public User deleteUser(User user) {
        return userDbStorage.delete(user);
    }

    public User updateUser(User user) {
        return userDbStorage.update(user);
    }

    public Collection<User> getAllUser() {
        return userDbStorage.getListUsers().values();
    }
}

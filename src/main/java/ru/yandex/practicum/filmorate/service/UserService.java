package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.inMemoryUserStorage = (InMemoryUserStorage) userStorage;
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
        if (!inMemoryUserStorage.getListUsers().containsKey(user.getId())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", user.getId()));
        }
    }

    public void validationIdUser(Long id) {
        if (!inMemoryUserStorage.getListUsers().containsKey(id.intValue())) {
            log.info("Нет такого идентификатора");
            throw new NotFoundException(String.format("Нет такого идентификатора № %s", id));
        }
    }

    public User getUser(Long id) {  //получение данных о пользователе по иго уникальному идентификатору
        return inMemoryUserStorage.getListUsers().get(id.intValue());
    }

    public void addFriend(Long id, Long friendId) {  //добавление в друзья
        inMemoryUserStorage.getListUsers().get(id.intValue()).getFriends().add(friendId);
        inMemoryUserStorage.getListUsers().get(friendId.intValue()).getFriends().add(id);
    }

    public void deleteFriend(Long id, Long friendId) {  //удаление из друзей
        inMemoryUserStorage.getListUsers().get(id.intValue()).getFriends().remove(friendId);
        inMemoryUserStorage.getListUsers().get(friendId.intValue()).getFriends().remove(id);
    }

    public List<User> getAllFriendsCurrentUser(Long id) { //возвращаем список пользователей, являющихся его друзьями
        List<Long> listFriendId = new ArrayList<>(inMemoryUserStorage.getListUsers().get(id.intValue()).getFriends());
        List<User> allFriends = new ArrayList<>();
        for (Long friendId : listFriendId) {
            allFriends.add(inMemoryUserStorage.getListUsers().get(friendId.intValue()));
        }
        return allFriends;
    }

    public List<User> getCommonFriendsList(Long id, Long otherId) {  //список друзей, общих с другим пользователем
        Set<Long> setId = new HashSet<>(inMemoryUserStorage.getListUsers().get(id.intValue()).getFriends());
        Set<Long> setFriendId = inMemoryUserStorage.getListUsers().get(otherId.intValue()).getFriends();
        setId.retainAll(setFriendId);

        List<User> commonFriends = new ArrayList<>();

        for (Long friendId : setId) {
            commonFriends.add(inMemoryUserStorage.getListUsers().get(friendId.intValue()));
        }
        return commonFriends;
    }

    public User addUser(User user) {
        return inMemoryUserStorage.add(user);
    }

    public User deleteUser(User user) {
        return inMemoryUserStorage.delete(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.update(user);
    }

    public Collection<User> getAllUser() {
        return inMemoryUserStorage.getListUsers().values();
    }
}

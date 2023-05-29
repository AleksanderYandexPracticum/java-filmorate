package ru.yandex.practicum.filmorate.service.user.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    public final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.inMemoryUserStorage = (InMemoryUserStorage) userStorage;
    }

    public void validationUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().indexOf("@") == -1) {
            log.info("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().indexOf(" ") != -1) {
            log.info("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public void validationIdUser(User user) throws ValidationException {
        if (!inMemoryUserStorage.getListUsers().containsKey(user.getId())) {
            log.info("Нет такого идентификатора");
            throw new ValidationException(String.format("Нет такого идентификатора № s%", user.getId()));
        }
    }

    public void validationIdUser(Long id) throws ValidationException {
        if (!inMemoryUserStorage.getListUsers().containsKey(id)) {
            log.info("Нет такого идентификатора");
            throw new ValidationException(String.format("Нет такого идентификатора № s%", id));
        }
    }

    public User getUser(Long id) {  //получение данных о пользователе по иго уникальному идентификатору
        validationIdUser(id);
        return inMemoryUserStorage.getListUsers().get(id);
    }

    public void addFriend(Long id, Long friendId) {  //добавление в друзья
        validationIdUser(id);
        validationIdUser(friendId);
        inMemoryUserStorage.getListUsers().get(id).getFriends().add(friendId);
        inMemoryUserStorage.getListUsers().get(friendId).getFriends().add(id);
    }

    public void deleteFriend(Long id, Long friendId) {  //удаление из друзей
        validationIdUser(id);
        validationIdUser(friendId);
        inMemoryUserStorage.getListUsers().get(id).getFriends().remove(friendId);
        inMemoryUserStorage.getListUsers().get(friendId).getFriends().remove(id);
    }

    public List<Long> getAllFriendsCurrentUser(Long id) { //возвращаем список пользователей, являющихся его друзьями
        validationIdUser(id);
        List<Long> list = new ArrayList<>(inMemoryUserStorage.getListUsers().get(id).getFriends());
        return list;
    }

    public List<Long> getListFriendsCurrentUser(Long id, Long friendId) {  //список друзей, общих с другим пользователем
        validationIdUser(id);
        validationIdUser(friendId);
        Set<Long> setId = inMemoryUserStorage.getListUsers().get(id).getFriends();
        Set<Long> setFriendId = inMemoryUserStorage.getListUsers().get(friendId).getFriends();
        setId.retainAll(setFriendId);
        if (setId.size() == 0) {
            throw new ValidationException("Общих друзей нет");
        }
        List<Long> list = new ArrayList<>(setId);
        return list;
    }
}

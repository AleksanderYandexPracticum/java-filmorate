package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDaoService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserDaoService userDaoService;

    @Autowired
    public UserController(@Qualifier("userDaoService") UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    public UserDaoService getUserDaoService() {
        return userDaoService;
    }

    @PostMapping
    public User addUser(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userDaoService.validationUser(user);
        User returnUser = userDaoService.addUser(user);
        return returnUser;
    }

    @DeleteMapping
    public User deleteUser(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userDaoService.validationUser(user);
        userDaoService.validationIdUser(user);
        User returnUser = userDaoService.deleteUser(user);
        return returnUser;
    }

    @PutMapping
    public User updateUser(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userDaoService.validationUser(user);
        userDaoService.validationIdUser(user);
        User returnUser = userDaoService.updateUser(user);
        return returnUser;
    }

    @GetMapping
    public Collection<User> getAllUser(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        return new ArrayList<>(userDaoService.getAllUser());
    }

    @GetMapping("/{id}")
    public User getUser(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userDaoService.validationIdUser(id);
        return userDaoService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(HttpServletRequest request,
                          @PathVariable("id") Long id,
                          @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userDaoService.validationIdUser(id);
        userDaoService.validationIdUser(friendId);
        userDaoService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(HttpServletRequest request,
                             @PathVariable("id") Long id,
                             @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userDaoService.validationIdUser(id);
        userDaoService.validationIdUser(friendId);
        userDaoService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriendsCurrentUser(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userDaoService.validationIdUser(id);
        return userDaoService.getAllFriendsCurrentUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(HttpServletRequest request,
                                           @PathVariable("id") Long id,
                                           @PathVariable("otherId") Long otherId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userDaoService.getCommonFriendsList(id, otherId);
    }
}

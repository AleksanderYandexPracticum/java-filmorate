package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    @PostMapping
    public User addUser(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.validationUser(user);
        User returnUser = userService.addUser(user);
        return returnUser;
    }

    @DeleteMapping
    public User deleteUser(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.validationUser(user);
        userService.validationIdUser(user);
        User returnUser = userService.deleteUser(user);
        return returnUser;
    }

    @PutMapping
    public User updateUser(HttpServletRequest request, @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.validationUser(user);
        userService.validationIdUser(user);
        User returnUser = userService.updateUser(user);
        return returnUser;
    }

    @GetMapping
    public Collection<User> getAllUser(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        return new ArrayList<>(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public User getUser(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.validationIdUser(id);
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(HttpServletRequest request,
                          @PathVariable("id") Long id,
                          @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.validationIdUser(id);
        userService.validationIdUser(friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(HttpServletRequest request,
                             @PathVariable("id") Long id,
                             @PathVariable("friendId") Long friendId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.validationIdUser(id);
        userService.validationIdUser(friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriendsCurrentUser(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        userService.validationIdUser(id);
        return userService.getAllFriendsCurrentUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(HttpServletRequest request,
                                           @PathVariable("id") Long id,
                                           @PathVariable("otherId") Long otherId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        return userService.getCommonFriendsList(id, otherId);
    }
}

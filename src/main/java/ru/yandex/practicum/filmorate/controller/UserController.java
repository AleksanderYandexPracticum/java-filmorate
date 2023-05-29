package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(HttpServletRequest request, @RequestBody User user) {
        userService.validationUser(user);
        User returnUser = userService.inMemoryUserStorage.add(user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return returnUser;
    }

    @DeleteMapping
    public User deleteUser(HttpServletRequest request, @RequestBody User user) {
        userService.validationUser(user);
        userService.validationIdUser(user);

        User returnUser = userService.inMemoryUserStorage.delete(user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return returnUser;
    }

    @PutMapping
    public User updateUser(HttpServletRequest request, @RequestBody User user) {
        userService.validationUser(user);
        userService.validationIdUser(user);
        User returnUser = userService.inMemoryUserStorage.update(user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return returnUser;
    }

    @GetMapping
    public Collection<User> getAllUser(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        return new ArrayList<>(userService.inMemoryUserStorage.getListUsers().values());
    }

    @GetMapping("/{id}")
    public User getUserInService(HttpServletRequest request, @PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return userService.getUser(id);
    }

}

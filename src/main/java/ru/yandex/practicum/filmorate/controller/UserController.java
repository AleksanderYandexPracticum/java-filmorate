package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> listUsers = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User addUser(HttpServletRequest request, @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().indexOf("@") == -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Электронная почта не может быть пустой и должна содержать символ @",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().indexOf(" ") != -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Логин не может быть пустым и содержать пробелы",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Дата рождения не может быть в будущем",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(++id);
        listUsers.put(id, user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return user;
    }

    @PutMapping
    public User updateUser(HttpServletRequest request, @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().indexOf("@") == -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Электронная почта не может быть пустой и должна содержать символ ",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().indexOf(" ") != -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Логин не может быть пустым и содержать пробелы",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Дата рождения не может быть в будущем",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (!listUsers.containsKey(user.getId())) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Нет такого идентификатора",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Нет такого идентификатора");
        }
        listUsers.put(user.getId(), user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return user;
    }

    @GetMapping
    public Collection<User> getAllUser(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());

        return listUsers.values();
    }
}

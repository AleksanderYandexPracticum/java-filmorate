package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {
    private HashMap<Integer, User> listUser = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User addUser(HttpServletRequest request, @Valid @RequestBody User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null || user.getEmail().indexOf("@") == -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Электронная почта не может быть пустой и должна содержать символ @",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().indexOf(" ") != -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Логин не может быть пустым и содержать пробелы",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank() || user.getName() == null) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Дата рождения не может быть в будущем",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(++id);
        listUser.put(id, user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return user;
    }

    @PutMapping
    public User updateUser(HttpServletRequest request, @Valid @RequestBody User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null || user.getEmail().indexOf("@") == -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Электронная почта не может быть пустой и должна содержать символ ",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().indexOf(" ") != -1) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Логин не может быть пустым и содержать пробелы",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isBlank() || user.getName() == null) user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Дата рождения не может быть в будущем",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (!listUser.containsKey(user.getId())) {
            log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}' " +
                            "Нет такого идентификатора",
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
            throw new ValidationException("Нет такого идентификатора");
        }
        listUser.put(user.getId(), user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return user;
    }

    @GetMapping
    public HashMap<Integer, User> getAllUser(HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return listUser;
    }
}

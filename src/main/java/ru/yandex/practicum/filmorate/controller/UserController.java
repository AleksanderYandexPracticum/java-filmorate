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
        validationUser(user);

        user.setId(++id);
        listUsers.put(id, user);
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        return user;
    }

    @PutMapping
    public User updateUser(HttpServletRequest request, @RequestBody User user) {
        validationUser(user);
        validationIdUser(user);

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

    protected void validationUser(User user) throws ValidationException {
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

    protected void validationIdUser(User user) throws ValidationException {
        if (!listUsers.containsKey(user.getId())) {
            log.info("Нет такого идентификатора");
            throw new ValidationException("Нет такого идентификатора");
        }
    }
}

package ru.yandex.practicum.filmorate.storage.user.UserStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    public User add(User user);

    public User delete(User user);

    public User update(User user);

    public Map<Integer, User> getAll();
}

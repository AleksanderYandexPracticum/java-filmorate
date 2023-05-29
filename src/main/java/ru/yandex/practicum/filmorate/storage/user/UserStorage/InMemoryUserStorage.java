package ru.yandex.practicum.filmorate.storage.user.UserStorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private HashMap<Integer, User> listUsers = new HashMap<>();
    private int id = 0;

    public HashMap<Integer, User> getListUsers() {
        return listUsers;
    }

    public int getId() {
        return id;
    }

    @Override
    public User add(User user) {
        user.setId(++id);
        listUsers.put(id, user);
        return user;
    }

    @Override
    public User delete(User user) {
        listUsers.remove(user.getId());
        return null;
    }

    @Override
    public User update(User user) {
        listUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public Map<Integer, User> getAll() {
        return listUsers;
    }
}

package ru.yandex.practicum.filmorate.storage;

import java.util.Map;

public interface Storage<T> {
    T add(T user);

    T delete(T user);

    T update(T user);

    Map<Integer, T> getAll();
}

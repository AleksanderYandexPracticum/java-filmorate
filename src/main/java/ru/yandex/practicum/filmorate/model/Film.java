package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private LinkedHashSet<Long> userIdsWhoLiked;  // Храним id тех кто лайкнул
    private LinkedHashSet<LinkedHashMap<String, Object>> genres; // Храним жанры
    private LinkedHashMap<String, Object> mpa;  // Храним рейтинг

    public Film(Integer id, String name, String description, LocalDate releaseDate, Long duration,
                LinkedHashSet<Long> userIdsWhoLiked, LinkedHashSet<LinkedHashMap<String, Object>> genres,
                LinkedHashMap<String, Object> mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.userIdsWhoLiked = userIdsWhoLiked;
        this.genres = genres;
        this.mpa = mpa;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", id);
        values.put("name", name);
        values.put("description", description);
        values.put("releaseDate", releaseDate);
        values.put("duration", duration);
        return values;
    }
}

package ru.yandex.practicum.filmorate.model;

public enum Genre {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    CARTOON("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик"),
    FANTASTIC("Фантастика"),
    FANTASY("Фэнтази"),
    HORRORS("Ужасы");

    private String title;

    Genre(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Жанр: " + title;
    }
}

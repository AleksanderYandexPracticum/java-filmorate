package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDaoService;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {
    private final FilmDbStorage filmDbStorage;
    private final FilmDaoService filmDaoService;
    private final UserDbStorage userDbStorage;

    @Test
    public void testFindUserById() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        Film filmFromBd = filmDbStorage.getListFilms().get(1);
        assertEquals(1, filmFromBd.getId());
        filmFromBd.setId(2);
        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> filmDaoService.validationIdFilm(filmFromBd));
        exception.getMessage();
        assertEquals("Нет такого идентификатора № 2", exception.getMessage());
    }

    @Test
    public void delete() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        Film deleteFilm = filmDbStorage.delete(film);
        assertEquals(1, deleteFilm.getId());
    }

    @Test
    public void update() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        LocalDate releaseDateUp = LocalDate.parse("1989-04-17");
        mpa = new LinkedHashMap<>();
        mpa.put("id", 2);
        Film filmUp = new Film(1, "Film Updated", "New film update decription", releaseDateUp, 190L,
                userIdsWhoLiked, genres, mpa);


        Film newFilm = filmDbStorage.update(filmUp);
        assertEquals(1, newFilm.getId());
        assertEquals("Film Updated", newFilm.getName());
        assertEquals("New film update decription", newFilm.getDescription());
        assertEquals(190L, newFilm.getDuration());
        assertEquals(releaseDateUp, newFilm.getReleaseDate());
        assertTrue(newFilm.getMpa().containsKey("id"));
        assertTrue(newFilm.getMpa().containsValue(2));
    }

    @Test
    public void getAll() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        LocalDate releaseDateUp = LocalDate.parse("1999-04-30");
        mpa = new LinkedHashMap<>();
        mpa.put("id", 3);
        genres = new LinkedHashSet<>();
        LinkedHashMap<String, Object> storageIdAndNameGenre = new LinkedHashMap<>();
        storageIdAndNameGenre.put("id", 1);
        genres.add(storageIdAndNameGenre);
        Film otherFilm = new Film(2, "New film", "New film update decription", releaseDateUp,
                120L, userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(otherFilm);

        HashMap<Integer, Film> allFilm = filmDbStorage.getListFilms();

        assertEquals(1, allFilm.get(1).getId());
        assertEquals("nisi eiusmod", allFilm.get(1).getName());
        assertEquals("adipisicing", allFilm.get(1).getDescription());
        assertEquals(100, allFilm.get(1).getDuration());
        assertEquals(releaseDate, allFilm.get(1).getReleaseDate());
        assertTrue(allFilm.containsKey(1));

        assertEquals(2, allFilm.get(2).getId());
        assertEquals("New film", allFilm.get(2).getName());
        assertEquals("New film update decription", allFilm.get(2).getDescription());
        assertEquals(120, allFilm.get(2).getDuration());
        assertEquals(releaseDateUp, allFilm.get(2).getReleaseDate());
        assertTrue(allFilm.containsKey(2));
    }

    @Test
    public void addLike() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        Set<Long> friends = new HashSet<>();
        Map<Long, Integer> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friends, friendships);
        userDbStorage.add(user);

        filmDbStorage.addLike(1L, 1L);
        Film filmFromDb = filmDbStorage.getListFilms().get(1);

        assertTrue(filmFromDb.getUserIdsWhoLiked().contains(1L));
    }

    @Test
    public void deleteLike() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        Set<Long> friends = new HashSet<>();
        Map<Long, Integer> friendships = new HashMap<>();
        LocalDate date = LocalDate.parse("1946-08-20");
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", date, friends, friendships);
        userDbStorage.add(user);

        filmDbStorage.addLike(1L, 1L);

        filmDbStorage.deleteLike(1L, 1L);

        Film filmFromDb = filmDbStorage.getListFilms().get(1);
        assertTrue(filmFromDb.getUserIdsWhoLiked().size() == 0);
    }

    @Test
    public void getMpa() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        mpa.put("id", 3);
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        LinkedHashMap<String, Object> mpaFromBd = filmDbStorage.getMpa(3);
        assertTrue(mpaFromBd.containsValue(3));

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> filmDbStorage.getMpa(7));
        exception.getMessage();
        assertEquals("Нет такого идентификатора № 7", exception.getMessage());
    }

    @Test
    public void getAllMpa() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        mpa.put("id", 3);
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        LocalDate releaseDateUp = LocalDate.parse("1999-04-30");
        mpa = new LinkedHashMap<>();
        mpa.put("id", 1);
        genres = new LinkedHashSet<>();
        LinkedHashMap<String, Object> storageIdAndNameGenre = new LinkedHashMap<>();
        storageIdAndNameGenre.put("id", 1);
        genres.add(storageIdAndNameGenre);
        Film otherFilm = new Film(2, "New film", "New film update decription", releaseDateUp,
                120L, userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(otherFilm);

        List<LinkedHashMap<String, Object>> allMpa = filmDbStorage.getAllMpa();

        assertTrue(allMpa.get(2).containsKey("id"));
        assertTrue(allMpa.get(2).containsValue(3));
        assertTrue(allMpa.get(2).containsKey("name"));
        assertTrue(allMpa.get(2).containsValue("PG-13"));


        assertTrue(allMpa.get(0).containsKey("id"));
        assertTrue(allMpa.get(0).containsValue(1));
        assertTrue(allMpa.get(0).containsKey("name"));
        assertTrue(allMpa.get(0).containsValue("G"));
    }

    @Test
    public void getGenresById() {
        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        mpa.put("id", 3);
        genres = new LinkedHashSet<>();
        LinkedHashMap<String, Object> storageIdAndNameGenre = new LinkedHashMap<>();
        storageIdAndNameGenre.put("id", 1);
        genres.add(storageIdAndNameGenre);

        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        LinkedHashMap<String, Object> genreFromBd = filmDbStorage.getGenresById(1);
        assertTrue(genreFromBd.containsValue(1));

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> filmDbStorage.getGenresById(7));
        exception.getMessage();
        assertEquals("Нет такого идентификатора № 7", exception.getMessage());
    }

    @Test
    public void getAllGenres() {

        LinkedHashSet<Long> userIdsWhoLiked = new LinkedHashSet<>();  // Храним id тех кто лайкнул
        LinkedHashSet<LinkedHashMap<String, Object>> genres = new LinkedHashSet<>(); // Храним жанры
        LinkedHashMap<String, Object> mpa = new LinkedHashMap<>();  // Храним рейтинг
        mpa.put("id", 3);
        LinkedHashMap<String, Object> storageIdAndNameGenre = new LinkedHashMap<>();
        storageIdAndNameGenre.put("id", 1);
        genres.add(storageIdAndNameGenre);
        LocalDate releaseDate = LocalDate.parse("1967-03-25");
        Film film = new Film(1, "nisi eiusmod", "adipisicing", releaseDate, 100L,
                userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(film);

        LocalDate releaseDateUp = LocalDate.parse("1999-04-30");
        mpa = new LinkedHashMap<>();
        mpa.put("id", 1);
        genres = new LinkedHashSet<>();
        storageIdAndNameGenre = new LinkedHashMap<>();
        storageIdAndNameGenre.put("id", 2);
        genres.add(storageIdAndNameGenre);
        Film otherFilm = new Film(2, "New film", "New film update decription", releaseDateUp,
                120L, userIdsWhoLiked, genres, mpa);
        filmDbStorage.add(otherFilm);

        LinkedHashSet<LinkedHashMap<String, Object>> allGenres = filmDbStorage.getAllGenres();

        int id = 1;
        for (LinkedHashMap<String, Object> allGenre : allGenres) {
            assertTrue(allGenre.containsKey("id"));
            assertTrue(allGenre.containsValue(id));
            id++;
        }

        String name = "Комедия";
        for (LinkedHashMap<String, Object> allGenre : allGenres) {
            assertTrue(allGenre.containsKey("name"));
            assertTrue(allGenre.containsValue(name));
            if (name.equals("Драма")) break;
            name = "Драма";
        }
    }
}
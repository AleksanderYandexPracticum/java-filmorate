package ru.yandex.practicum.filmorate.controller;

//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

//    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
//    UserService userService = new UserService(inMemoryUserStorage);
//    UserController userController = new UserController(userService);
//
//    @Test
//    void addUser() {
//        User user = new User();
//        user.setLogin("dolore");
//        user.setName("Nick Name");
//        user.setEmail("");
//        user.setBirthday(LocalDate.of(1946, 8, 20));
//        final ValidationException exception = assertThrows(ValidationException.class,
//                () -> userController.getUserDaoService().validationUser(user));
//        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
//
//        User user1 = new User();
//        user1.setLogin(" ");
//        user1.setName("Nick Name");
//        user1.setEmail("mail@mail.ru");
//        user1.setBirthday(LocalDate.of(1946, 8, 20));
//        final ValidationException exception1 = assertThrows(ValidationException.class,
//                () -> userController.getUserDaoService().validationUser(user1));
//        assertEquals("Логин не может быть пустым и содержать пробелы", exception1.getMessage());
//
//        User user2 = new User();
//        user2.setLogin("dolore");
//        user2.setName("");
//        user2.setEmail("mail@mail.ru");
//        user2.setBirthday(LocalDate.of(1946, 8, 20));
//
//        userController.getUserDaoService().validationUser(user2);
//
//        assertEquals(user2.getName(), user2.getLogin());
//
//        User user3 = new User();
//        user3.setLogin("dolore");
//        user3.setName("Nick Name");
//        user3.setEmail("mail@mail.ru");
//        user3.setBirthday(LocalDate.of(2030, 8, 20));
//        final ValidationException exception3 = assertThrows(ValidationException.class,
//                () -> userController.getUserDaoService().validationUser(user3));
//        assertEquals("Дата рождения не может быть в будущем", exception3.getMessage());
//    }
//
//    @Test
//    void updateUser() {
//        User user = new User();
//        user.setLogin("dolore");
//        user.setName("Nick Name");
//        user.setEmail("");
//        user.setBirthday(LocalDate.of(1946, 8, 20));
//        final ValidationException exception = assertThrows(ValidationException.class,
//                () -> userController.getUserDaoService().validationUser(user));
//        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
//
//        User user1 = new User();
//        user1.setLogin(" ");
//        user1.setName("Nick Name");
//        user1.setEmail("mail@mail.ru");
//        user1.setBirthday(LocalDate.of(1946, 8, 20));
//        final ValidationException exception1 = assertThrows(ValidationException.class,
//                () -> userController.getUserDaoService().validationUser(user1));
//        assertEquals("Логин не может быть пустым и содержать пробелы", exception1.getMessage());
//
//        User user2 = new User();
//        user2.setLogin("dolore");
//        user2.setName("");
//        user2.setEmail("mail@mail.ru");
//        user2.setBirthday(LocalDate.of(1946, 8, 20));
//
//        userController.getUserDaoService().validationUser(user2);
//
//        assertEquals(user2.getName(), user2.getLogin());
//
//        User user3 = new User();
//        user3.setLogin("dolore");
//        user3.setName("Nick Name");
//        user3.setEmail("mail@mail.ru");
//        user3.setBirthday(LocalDate.of(2030, 8, 20));
//        final ValidationException exception3 = assertThrows(ValidationException.class,
//                () -> userController.getUserDaoService().validationUser(user3));
//        assertEquals("Дата рождения не может быть в будущем", exception3.getMessage());
//
//        User user4 = new User();
//        user4.setId(4);
//        user4.setLogin("dolore");
//        user4.setName("Nick Name");
//        user4.setEmail("mail@mail.ru");
//        user4.setBirthday(LocalDate.of(2030, 8, 20));
//        final NotFoundException exception4 = assertThrows(NotFoundException.class,
//                () -> userController.getUserDaoService().validationIdUser(user4));
//        exception4.getMessage();
//        assertEquals("Нет такого идентификатора № 4", exception4.getMessage());
//    }
}
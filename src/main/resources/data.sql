--INSERT INTO users (email, login, name)
--SELECT * FROM (SELECT 'john@mail.ru' AS email, 'robot' AS login, 'robobot' AS name)
--WHERE NOT EXISTS (
--SELECT email FROM users WHERE email='john@mail.ru'
--);
--DELETE FROM users;

INSERT INTO MPA (MPA_ID, NAME) VALUES (1, 'G');
INSERT INTO MPA (MPA_ID, NAME) VALUES (2, 'PG');
INSERT INTO MPA (MPA_ID, NAME) VALUES (3, 'PG-13');
INSERT INTO MPA (MPA_ID, NAME) VALUES (4, 'R');
INSERT INTO MPA (MPA_ID, NAME) VALUES (5, 'NC-17');

INSERT INTO GENRE (GENRE_ID, NAME) VALUES (1, 'Комедия');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (2, 'Драма');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (3, 'Мультфильм');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (4, 'Триллер');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (5, 'Документальный');
INSERT INTO GENRE (GENRE_ID, NAME) VALUES (6, 'Боевик');
--INSERT INTO GENRE (GENRE_ID, NAME) VALUES (7, 'Фантастика');
--INSERT INTO GENRE (GENRE_ID, NAME) VALUES (8, 'Фэнтази');
--INSERT INTO GENRE (GENRE_ID, NAME) VALUES (9, 'Ужасы');
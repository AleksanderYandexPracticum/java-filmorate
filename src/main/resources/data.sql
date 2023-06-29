--INSERT INTO users (email, login, name)
--SELECT * FROM (SELECT 'john@mail.ru' AS email, 'robot' AS login, 'robobot' AS name)
--WHERE NOT EXISTS (
--SELECT email FROM users WHERE email='john@mail.ru'
--);
DELETE FROM users;
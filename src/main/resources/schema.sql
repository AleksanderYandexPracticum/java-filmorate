DROP TABLE friends;
DROP TABLE userWhoLiked;
DROP TABLE friendships;
DROP TABLE users;
DROP TABLE film_genre;
DROP TABLE films;
DROP TABLE genre;
DROP TABLE mpa;


Create table IF NOT EXISTS mpa
(
mpa_id int generated by default as identity primary key,
name  varchar(100) not null
);

Create table IF NOT EXISTS genre
(
genre_id int generated by default as identity primary key,
name  varchar(100) not null
);

Create table IF NOT EXISTS films
(
film_id int generated by default as identity primary key,
name  varchar(50) not null,
description varchar(300),
releaseDate timestamp,
duration long not null,
mpa_id int REFERENCES mpa (mpa_id) ON DELETE CASCADE
);

Create table IF NOT EXISTS film_genre
(
film_id int REFERENCES films (film_id) ON DELETE CASCADE,
genre_id  int REFERENCES genre (genre_id) ON DELETE CASCADE
);

create table IF NOT EXISTS users
(
user_id int generated by default as identity primary key,
email  varchar(50) not null,
login varchar(300) not null,
name varchar(50) not null,
birthday date
);

Create table IF NOT EXISTS friendships
(
friendships_id int generated by default as identity primary key,
friend_id int,
confirmation int
);

Create table IF NOT EXISTS userWhoLiked
(
film_id int REFERENCES films (film_id) ON DELETE CASCADE,
user_id int REFERENCES users (user_id) ON DELETE CASCADE
);

Create table IF NOT EXISTS friends
(
user_id int REFERENCES users (user_id) ON DELETE CASCADE,
friendships_id int REFERENCES friendships (friendships_id) ON DELETE CASCADE
);
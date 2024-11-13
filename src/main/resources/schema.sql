-- user_status - recreated each launch for population of initial data
DROP TABLE IF EXISTS user_status;

CREATE TABLE IF NOT EXISTS user_status(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- friendship_status - recreated each launch for population of initial data
DROP TABLE IF EXISTS friendship_status;

CREATE TABLE IF NOT EXISTS friendship_status(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- film_status - recreated each launch for population of initial data
DROP TABLE IF EXISTS film_status;

CREATE TABLE IF NOT EXISTS film_status(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- likes_status - recreated each launch for population of initial data
DROP TABLE IF EXISTS likes_status;

CREATE TABLE IF NOT EXISTS likes_status(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- users
CREATE TABLE IF NOT EXISTS users(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email varchar NOT NULL,
name varchar,
login varchar NOT NULL,
birthday date,
status_id bigint NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp(),

foreign key (status_id) references user_status(id));

-- friends - friendship log
CREATE TABLE IF NOT EXISTS friends(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id bigint NOT NULL,
friend_id bigint NOT NULL,
status_id bigint NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp,

foreign key (user_id) references users(id),
foreign key (friend_id) references users(id),
foreign key (status_id) references friendship_status(id));

-- genre - genre types
CREATE TABLE IF NOT EXISTS genre(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- rating - genre types
CREATE TABLE IF NOT EXISTS rating(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- films
CREATE TABLE IF NOT EXISTS films(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar NOT NULL,
description varchar,
release_date date,
duration int,
genre_id bigint,
rating_id bigint,
status_id bigint not null,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp,

foreign key (genre_id) references genre(id),
foreign key (rating_id) references rating(id),
foreign key (status_id) references film_status(id));

-- likes - log
CREATE TABLE IF NOT EXISTS likes(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id bigint,
film_id bigint,
status_id bigint not null,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp,

foreign key (user_id) references users(id),
foreign key (film_id) references films(id),
foreign key (status_id) references likes_status(id));
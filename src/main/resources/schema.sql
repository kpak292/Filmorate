-- user_status - recreated each launch for population of initial data
CREATE TABLE IF NOT EXISTS entry_status(
id bigint PRIMARY KEY,
name varchar(200) NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- friendship_status - recreated each launch for population of initial data
CREATE TABLE IF NOT EXISTS friendship_status(
id bigint  PRIMARY KEY,
name varchar(200) NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- rating - rating types
CREATE TABLE IF NOT EXISTS rating(
id bigint PRIMARY KEY,
name varchar(200) NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- genre - genre types
CREATE TABLE IF NOT EXISTS genre(
id bigint  PRIMARY KEY,
name varchar(200) NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp);

-- users
CREATE TABLE IF NOT EXISTS users(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email varchar(200) NOT NULL,
name varchar(200),
login varchar(200) NOT NULL,
birthday date,
status_id bigint default 0 NOT NULL,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp(),

foreign key (status_id) references entry_status(id));

create unique index if not exists USER_EMAIL_UINDEX on USERS (email);
create unique index if not exists USER_LOGIN_UINDEX on USERS (login);

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

-- films
CREATE TABLE IF NOT EXISTS films(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(200) NOT NULL,
description varchar(200),
release_date date,
duration int,
rating_id bigint,
status_id bigint default 0 not null,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp,

foreign key (rating_id) references rating(id),
foreign key (status_id) references entry_status(id));

-- likes - log
CREATE TABLE IF NOT EXISTS likes(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id bigint,
film_id bigint,
status_id bigint default 0 not null,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp,

foreign key (user_id) references users(id),
foreign key (film_id) references films(id),
foreign key (status_id) references entry_status(id));

--genres - many to many
CREATE TABLE IF NOT EXISTS genres(
id bigint  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
film_id bigint,
genre_id bigint,
status_id bigint default 0 not null,
created_at timestamp default current_timestamp(),
updated_at timestamp default current_timestamp on update current_timestamp,

foreign key (film_id) references films(id),
foreign key (status_id) references entry_status(id),
foreign key (genre_id) references genre(id));
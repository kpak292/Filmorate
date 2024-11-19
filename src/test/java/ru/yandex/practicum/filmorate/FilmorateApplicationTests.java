package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.impl.DBFilmRepository;
import ru.yandex.practicum.filmorate.dal.impl.DBUserRepository;
import ru.yandex.practicum.filmorate.entities.Film;
import ru.yandex.practicum.filmorate.entities.Genre;
import ru.yandex.practicum.filmorate.entities.Mpa;
import ru.yandex.practicum.filmorate.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan
@Import({DBUserRepository.class})
class FilmoRateApplicationTests {
    @Autowired
    private final DBUserRepository userRepository;

    private final DBFilmRepository filmRepository;

    @Test
    public void testFindAllUsers() {
        User newUser = User.builder()
                .email("test@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();

        User user1 = userRepository.create(newUser);

        Collection<User> users = userRepository.getAll();

        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void testCreateUser() {
        User newUser = User.builder()
                .email("test@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();
        User user = userRepository.create(newUser);

        assertThat(user).hasFieldOrPropertyWithValue("id", user.getId());
    }

    @Test
    public void testFindUserById() {
        User newUser = User.builder()
                .email("test@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();
        User testUser = userRepository.create(newUser);

        User user = userRepository.getById(testUser.getId());


        assertThat(user).hasFieldOrPropertyWithValue("id", testUser.getId());
    }

    @Test
    public void testUpdateUser() {
        User newUser = User.builder()
                .email("test@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();
        User testUser = userRepository.create(newUser);
        testUser.setName("Updated");

        User user = userRepository.update(testUser);

        user = userRepository.getById(user.getId());

        assertThat(user).hasFieldOrPropertyWithValue("name", "Updated");
    }

    @Test
    public void testDeleteUser() {
        User newUser = User.builder()
                .email("test@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();
        User testUser = userRepository.create(newUser);

        User user = userRepository.delete(testUser.getId());
        Collection<User> users = userRepository.getAll();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void testCreateFilms() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        Genre genre = new Genre();
        genre.setId(2);
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Film film = Film.builder()
                .mpa(mpa)
                .genres(genres)
                .name("testFilm")
                .description("testDescription")
                .releaseDate(LocalDate.of(1990, 12, 12))
                .duration(120)
                .build();

        Film result = filmRepository.create(film);

        assertThat(result).hasFieldOrPropertyWithValue("id", result.getId());
    }

    @Test
    public void testFindByIdFilms() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        Genre genre = new Genre();
        genre.setId(2);
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Film film = Film.builder()
                .mpa(mpa)
                .genres(genres)
                .name("testFilm")
                .description("testDescription")
                .releaseDate(LocalDate.of(1990, 12, 12))
                .duration(120)
                .build();

        film = filmRepository.create(film);

        Film result = filmRepository.getById(film.getId());

        assertThat(result).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    public void testFindAllFilms() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        Genre genre = new Genre();
        genre.setId(2);
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Film film = Film.builder()
                .mpa(mpa)
                .genres(genres)
                .name("testFilm")
                .description("testDescription")
                .releaseDate(LocalDate.of(1990, 12, 12))
                .duration(120)
                .build();

        film = filmRepository.create(film);

        Collection<Film> films = filmRepository.findAll();

        assertThat(films.size()).isEqualTo(1);
    }

    @Test
    public void testDeleteFilms() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        Genre genre = new Genre();
        genre.setId(2);
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Film film = Film.builder()
                .mpa(mpa)
                .genres(genres)
                .name("testFilm")
                .description("testDescription")
                .releaseDate(LocalDate.of(1990, 12, 12))
                .duration(120)
                .build();

        film = filmRepository.create(film);

        Collection<Film> films = filmRepository.findAll();

        assertThat(films.size()).isEqualTo(1);

        filmRepository.delete(film.getId());

        films = filmRepository.findAll();

        assertThat(films.size()).isEqualTo(0);
    }

    @Test
    public void testLikeFilms() {
        Mpa mpa = new Mpa();
        mpa.setId(2);
        Genre genre = new Genre();
        genre.setId(2);
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);

        Film film = Film.builder()
                .mpa(mpa)
                .genres(genres)
                .name("testFilm")
                .description("testDescription")
                .releaseDate(LocalDate.of(1990, 12, 12))
                .duration(120)
                .build();

        User newUser = User.builder()
                .email("test@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();
        User user = userRepository.create(newUser);

        film = filmRepository.create(film);

        Collection<Film> films = filmRepository.getTop(5);

        assertThat(films.size()).isEqualTo(0);

        filmRepository.addLike(film.getId(), user.getId());

        films = filmRepository.getTop(5);

        assertThat(films.size()).isEqualTo(1);

        filmRepository.removeLike(film.getId(), user.getId());

        films = filmRepository.getTop(5);

        assertThat(films.size()).isEqualTo(0);
    }
}
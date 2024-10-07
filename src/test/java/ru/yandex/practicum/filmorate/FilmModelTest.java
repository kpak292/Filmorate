package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmModelTest {
    private final FilmController validator = new FilmController();

    @Test
    public void shouldNotCreateFilmWithNullName() {
        Film film = Film.builder()
                .name(null)
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1990, 12, 2))
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(film));
    }

    @Test
    public void shouldNotValidateFilmWithBlankName() {
        Film film = Film.builder()
                .name(" ")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1990, 12, 2))
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(film));
    }

    @Test
    public void shouldNotValidateFilmWithLongDescription() {
        Film film = Film.builder()
                .name("name")
                .description("a".repeat(201))
                .duration(100)
                .releaseDate(LocalDate.of(1990, 12, 2))
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(film));
    }

    @Test
    public void shouldNotValidateFilmWithNegativeDuration() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(-100)
                .releaseDate(LocalDate.of(1990, 12, 2))
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(film));
    }

    @Test
    public void shouldNotValidateFilmWithReleaseDateBefore28Dec1985() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1880, 12, 2))
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(film));
    }

    @Test
    public void shouldNotValidateFilmWithReleaseDateInFuture() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(2990, 12, 2))
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(film));
    }

}

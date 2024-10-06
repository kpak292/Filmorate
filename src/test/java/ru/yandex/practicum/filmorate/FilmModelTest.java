package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmModelTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldNotCreateFilmWithNullName() {
        assertThrows(NullPointerException.class, () -> new Film(null));
    }

    @Test
    public void shouldNotValidateFilmWithBlankName() {
        Film film = new Film(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotValidateFilmWithLongDescription() {
        Film film = new Film("test");
        film.setDescription("a".repeat(201));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotValidateFilmWithNegativeDuration() {
        Film film = new Film("test");
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotValidateFilmWithReleaseDateBefore28Dec1985() {
        Film film = new Film("test");
        film.setReleaseDate(LocalDate.of(1900, 11, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotValidateFilmWithReleaseDateInFuture() {
        Film film = new Film("test");
        film.setReleaseDate(LocalDate.of(2900, 11, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
    }
}

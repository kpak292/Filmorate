package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.impl.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.entities.User;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserModelTest {
    private final UserRepository validator = new InMemoryUserRepository();

    @Test
    public void shouldNotValidateUserWithBlankLogin() {
        User user = User.builder()
                .login(" ")
                .birthday(LocalDate.of(1990, 12, 1))
                .email("test@test.ru")
                .name("name")
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(user));
    }

    @Test
    public void shouldNotCreateUserWithNullLogin() {
        User user = User.builder()
                .login(null)
                .birthday(LocalDate.of(1990, 12, 1))
                .email("test@test.ru")
                .name("name")
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(user));
    }

    @Test
    public void shouldNotCreateUserWithNullEmail() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.of(1990, 12, 1))
                .email(null)
                .name("name")
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(user));
    }

    @Test
    public void shouldNotValidateUserWithBlankEmail() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.of(1990, 12, 1))
                .email(" ")
                .name("name")
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(user));
    }

    @Test
    public void shouldNotValidateUserWithIncorrectEmail() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.of(1990, 12, 1))
                .email("test")
                .name("name")
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(user));
    }

    @Test
    public void shouldNotValidateUserWithIncorrectEmail2() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.of(1990, 12, 1))
                .email("@test.ru")
                .name("name")
                .build();

        Validator validator2 = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> violations = validator2.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotValidateUserWithIncorrectBirthDate() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.of(2990, 12, 1))
                .email("test@test.ru")
                .name("name")
                .build();

        assertThrows(ValidationException.class,
                () -> validator.create(user));
    }
}

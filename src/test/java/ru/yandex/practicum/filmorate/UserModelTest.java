package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserModelTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldNotValidateUserWithBlankLogin() {
        User user = new User("test@test.ru", " ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotCreateUserWithNullLogin() {
        assertThrows(NullPointerException.class, () -> new User("test@test.ru", null));
    }

    @Test
    public void shouldNotCreateUserWithNullEmail() {
        assertThrows(NullPointerException.class, () -> new User(null, "login"));
    }

    @Test
    public void shouldNotValidateUserWithBlankEmail() {
        User user = new User(" ", "login");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotValidateUserWithIncorrectEmail() {
        User user = new User("@asdfasf.ru", "login");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldNotValidateUserWithIncorrectBirthDate() {
        User user = new User("test@test.ru", "login");
        user.setBirthday(LocalDate.of(2100, 12, 2));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

}

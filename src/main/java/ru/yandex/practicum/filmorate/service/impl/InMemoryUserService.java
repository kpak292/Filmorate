package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserDAO;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final InMemoryUserDAO repository;

    @Override
    public Collection<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User create(User user) {
        if (!validate(user)) {
            log.debug("Create: Validation failed for user: {}", user.toString());
            throw new ValidationException("Error: Data does not meet requirements");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Create: User name is blank or null, set name = login");
            user.setName(user.getLogin());
        }

        return repository.create(user);
    }

    @Override
    public User update(User user) {
        if (!validate(user)) {
            log.debug("Update: Validation failed for user: {}", user.toString());
            throw new ValidationException("Error: Data does not meet requirements");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("User name is blank or null, set name = login");
            user.setName(user.getLogin());
        }

        return repository.update(user);
    }

    //Проверка на полей на критерии
    @Override
    public boolean validate(User user) {
        return user.getLogin() != null &&
                user.getEmail() != null &&
                !user.getEmail().isBlank() &&
                user.getEmail().contains("@") &&
                !user.getLogin().isBlank() &&
                user.getBirthday().isBefore(LocalDate.now());
    }
}

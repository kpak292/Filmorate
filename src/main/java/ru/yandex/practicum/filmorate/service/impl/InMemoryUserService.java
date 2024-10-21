package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserDAO;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserDAO repository;

    @Override
    public Collection<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User getById(long id) {
        if (id <= 0) {
            log.debug("GetById: Validation failed for ID");
            throw new ValidationException("Error: ID should be positive");
        }
        return repository.getById(id);
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
            log.debug("Update: User name is blank or null, set name = login");
            user.setName(user.getLogin());
        }

        return repository.update(user);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        if (userId <= 0 || friendId <= 0) {
            log.debug("addFriend: Validation failed for ID");
            throw new ValidationException("Error: ID should be positive");
        }

        repository.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        if (userId <= 0 || friendId <= 0) {
            log.debug("removeFriend: Validation failed for ID");
            throw new ValidationException("Error: ID should be positive");
        }

        repository.removeFriend(userId, friendId);
    }

    @Override
    public Collection<User> getFriends(long id) {
        if (id <= 0) {
            log.debug("getFriends: Validation failed for ID");
            throw new ValidationException("Error: ID should be positive");
        }

        return repository.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long friendId) {
        Collection<User> userFriends = repository.getFriends(userId);
        Collection<User> friendFriends = repository.getFriends(friendId);

        return userFriends.stream()
                .filter(friendFriends::contains)
                .toList();
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

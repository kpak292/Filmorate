package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        log.info("Received GET request");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Received POST request: {}", user.toString());
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("User name is blank or null, set name = login");
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        log.debug("Generated ID for user: {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Added user: {}", user.toString());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Received PUT request: {}", user.toString());
        if (!users.containsKey(user.getId())) {
            log.debug("User is not found with id {}", user.getId());
            throw new NotFoundException("Error: User with id " + user.getId() + " is not found");
        }

        User oldUser = users.get(user.getId());
        oldUser.setLogin(user.getLogin());

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("User name is blank or null, set name = login");
            oldUser.setName(user.getLogin());
        } else {
            oldUser.setName(user.getName());
        }

        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        log.debug("Updated user: {}", oldUser.toString());

        return oldUser;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

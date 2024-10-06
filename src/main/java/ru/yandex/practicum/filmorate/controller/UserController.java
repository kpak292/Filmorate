package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (!validate(user)) {
            throw new ValidationException("Error: Data does not meet requirements");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Error: User with id " + user.getId() + " is not found");
        }

        if (!validate(user)) {
            throw new ValidationException("Error: Data does not meet requirements");
        }

        User oldUser = users.get(user.getId());
        oldUser.setLogin(user.getLogin());

        if (user.getName() == null || user.getName().isBlank()) {
            oldUser.setName(user.getLogin());
        } else {
            oldUser.setName(user.getName());
        }

        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());

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

    //Проверка на полей на критерии
    private boolean validate(User user) {
        return !user.getEmail().isBlank() &&
                user.getEmail().contains("@") &&
                !user.getLogin().isBlank() &&
                user.getBirthday().isBefore(LocalDate.now());
    }

}

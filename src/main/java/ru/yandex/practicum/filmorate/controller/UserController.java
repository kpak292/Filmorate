package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> getAll() {
        log.info("Received GET request");
        return service.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Received POST request: {}", user.toString());

        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Received PUT request: {}", user.toString());

        return service.update(user);
    }

}

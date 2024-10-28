package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public ResponseEntity<Collection<User>> getAll() {
        return new ResponseEntity<>(
                service.getAll(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable long id) {
        return new ResponseEntity<>(
                service.getById(id),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return new ResponseEntity<>(
                service.create(user),
                HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return new ResponseEntity<>(
                service.update(user),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getFriends(@PathVariable long id) {
        return new ResponseEntity<>(
                service.getFriends(id),
                HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Collection<User>> addFriend(@PathVariable long id, @PathVariable long friendId) {
        service.addFriend(id, friendId);

        return new ResponseEntity<>(
                service.getFriends(id),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Collection<User>> removeFriend(@PathVariable long id, @PathVariable long friendId) {
        service.removeFriend(id, friendId);

        return new ResponseEntity<>(
                service.getFriends(id),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return new ResponseEntity<>(
                service.getCommonFriends(id, otherId),
                HttpStatus.OK);
    }

}

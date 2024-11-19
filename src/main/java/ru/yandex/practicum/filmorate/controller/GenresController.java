package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.entities.Genre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenresController {
    private final GenresService service;

    @GetMapping()
    public ResponseEntity<Collection<Genre>> getAll() {
        return new ResponseEntity<>(
                service.getAll(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable long id) {
        return new ResponseEntity<>(
                service.getById(id),
                HttpStatus.OK);
    }


}

package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Received GET request");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Received POST request: {}", film.toString());
        film.setId(getNextId());
        log.debug("Generated ID for film: {}", film.getId());
        films.put(film.getId(), film);
        log.debug("Added film: {}", film.toString());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Received PUT request: {}", film.toString());
        if (!films.containsKey(film.getId())) {
            log.debug("Film is not found with id {}", film.getId());
            throw new NotFoundException("Error: Film with id " + film.getId() + " is not found");
        }

        Film oldFilm = films.get(film.getId());
        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
        log.debug("Updated user: {}", oldFilm.toString());

        return oldFilm;
    }

    // вспомогательный метод для генерации идентификатора нового фильма
    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}

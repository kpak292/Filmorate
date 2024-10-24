package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmDAO;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final InMemoryFilmDAO repository;

    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    @Override
    public Collection<Film> findAll() {
        return repository.findAll();
    }

    @Override
    public Film create(Film film) {
        if (!validate(film)) {
            log.debug("Create: Validation failed film: {}", film.toString());
            throw new ValidationException("Error: Data does not meet requirements");
        }

        return repository.create(film);
    }

    @Override
    public Film update(Film film) {
        if (!validate(film)) {
            log.debug("Update: Validation failed film: {}", film.toString());
            throw new ValidationException("Error: Data does not meet requirements");
        }

        return repository.update(film);
    }

    //Проверка на полей на критерии
    @Override
    public boolean validate(Film film) {
        return film.getName() != null &&
                !film.getName().isBlank() &&
                film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(minDate) &&
                film.getReleaseDate().isBefore(LocalDate.now()) &&
                film.getDuration() >= 0;
    }

}

package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.InMemoryFilmDAO;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final InMemoryFilmDAO repository;

    @Override
    public Collection<Film> findAll() {
        return repository.findAll();
    }

    @Override
    public Film create(Film film) {
        return repository.create(film);
    }

    @Override
    public Film update(Film film) {
        return repository.update(film);
    }

    @Override
    public Film delete(long id) {
        return repository.delete(id);
    }

    @Override
    public Film getById(long id) {
        return repository.getById(id);
    }

    @Override
    public Map<Film, Integer> addLike(long filmId, long userId) {
        return repository.addLike(filmId, userId);
    }

    @Override
    public Map<Film, Integer> removeLike(long filmId, long userId) {
        return repository.removeLike(filmId, userId);
    }

    @Override
    public Collection<Film> getTop(int count) {
        return repository.getTop(count);
    }

}

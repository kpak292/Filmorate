package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmService {
    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film delete(long id);

    Film getById(long id);

    Map<Film, Integer> addLike(long filmId, long userId);

    Map<Film, Integer> removeLike(long filmId, long userId);

    Collection<Film> getTop(int count);
}

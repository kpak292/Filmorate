package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.entities.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmDAO {
    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getById(long id);

    Film delete(long id);

    Map<Film, Integer> addLike(long filmId, long userId);

    Map<Film, Integer> removeLike(long filmId, long userId);

    Collection<Film> getTop(int count);

    void validate(Film films);
}

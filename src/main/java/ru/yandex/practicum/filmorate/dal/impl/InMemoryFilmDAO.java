package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmDAO;
import ru.yandex.practicum.filmorate.dal.UserDAO;
import ru.yandex.practicum.filmorate.entities.Film;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryFilmDAO implements FilmDAO {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Collection<Long>> likes = new HashMap<>();

    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    @Autowired
    private final UserDAO users;

    public InMemoryFilmDAO(UserDAO users) {
        this.users = users;
    }

    @Override
    public Collection<Film> findAll() {
        log.debug("FilmDAO/getAll");
        return films.values();
    }

    @Override
    public Film create(Film film) {
        validate(film);

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("FilmDAO/create - Added film: {}", film.toString());
        return film;
    }

    @Override
    public Film update(Film film) {
        validate(film.getId());

        Film oldFilm = films.get(film.getId());
        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
        log.debug("FilmDAO/update - Updated film: {}", oldFilm.toString());

        return oldFilm;
    }

    @Override
    public Film getById(long id) {
        validate(id);

        log.debug("FilmDAO/getById - Film ID: {}", id);
        return films.get(id);
    }

    @Override
    public Film delete(long id) {
        validate(id);

        log.debug("FilmDAO/delete - removed Film ID: {}", id);

        //Delete all likes
        likes.remove(id);
        likes.values().forEach(array -> array.remove(id));

        return films.remove(id);
    }

    @Override
    public Map<Film, Integer> addLike(long filmId, long userId) {
        validate(filmId);


        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }

        likes.get(filmId).add(userId);

        log.debug("FilmDAO/addLike - added like for Film ID {} from user ID {}", filmId, userId);

        return Map.of(films.get(filmId),
                likes.get(filmId).size());
    }

    @Override
    public Map<Film, Integer> removeLike(long filmId, long userId) {
        validate(filmId);

        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }

        likes.get(filmId).remove(userId);

        log.debug("FilmDAO/removeLike - removed like for Film ID {} from user ID {}", filmId, userId);

        return Map.of(films.get(filmId),
                likes.get(filmId).size());
    }

    @Override
    public Collection<Film> getTop(int count) {
        log.debug("FilmDAO/getTop");

        return likes.entrySet().stream()
                .filter(list -> !list.getValue().isEmpty())
                .map(entry -> Map.entry(films.get(entry.getKey()),
                        entry.getValue().size()))
                .sorted(new Comparator<Map.Entry<Film, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Film, Integer> o1, Map.Entry<Film, Integer> o2) {
                        return o2.getValue() - o1.getValue();
                    }
                })
                .limit(count)
                .map(Map.Entry::getKey)
                .toList();
    }

    // вспомогательный метод для генерации идентификатора нового фильма
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


    @Override
    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Film:name - name cannot be blank or null");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Film:description - description cannot be more than 200 symbols");
        }

        if (film.getReleaseDate().isBefore(minDate) || film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Film:releaseDate - release date cannot be in future or before 28.12.1895");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Film:duration - duration cannot be negative");
        }
    }

    public void validate(long... ids) {
        String notFound = Arrays.stream(ids)
                .filter(id -> !films.containsKey(id))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        if (!notFound.isBlank()) {
            throw new NotFoundException("Film with ID " + notFound + " is not found");
        }
    }

}

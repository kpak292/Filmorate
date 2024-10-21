package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmDAO;
import ru.yandex.practicum.filmorate.repository.UserDAO;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryFilmDAO implements FilmDAO {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Collection<Long>> likes = new HashMap<>();

    @Autowired
    private final UserDAO users;

    public InMemoryFilmDAO(UserDAO users) {
        this.users = users;
    }

    @Override
    public Collection<Film> findAll() {
        log.debug("Film/GetAll");
        return films.values();
    }

    @Override
    public Film create(Film film) {
        log.debug("Film/Create");
        film.setId(getNextId());
        log.debug("Generated id: {}", film.getId());
        films.put(film.getId(), film);
        log.debug("Added film: {}", film.toString());
        return film;
    }

    @Override
    public Film update(Film film) {
        log.debug("Film/Update: id {}", film.getId());
        checkFilms(film.getId());

        Film oldFilm = films.get(film.getId());
        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
        log.debug("Updated user: {}", oldFilm.toString());

        return oldFilm;
    }

    @Override
    public Film getById(long id) {
        checkFilms(id);

        return films.get(id);
    }

    @Override
    public Map<Film, Integer> addLike(long filmId, long userId) {
        log.debug("Film/addLike: ids {}, {}", filmId, userId);

        checkFilms(filmId);
        users.checkUsers(userId);

        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }

        likes.get(filmId).add(userId);

        return Map.of(films.get(filmId),
                likes.get(filmId).size());
    }

    @Override
    public Map<Film, Integer> removeLike(long filmId, long userId) {
        log.debug("Film/removeLike: ids {}, {}", filmId, userId);

        checkFilms(filmId);
        users.checkUsers(userId);

        if (!likes.containsKey(filmId)) {
            likes.put(filmId, new HashSet<>());
        }

        likes.get(filmId).remove(userId);

        return Map.of(films.get(filmId),
                likes.get(filmId).size());
    }

    @Override
    public Collection<Film> getTop(int count) {
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

    // Util method for check films
    @Override
    public void checkFilms(long... ids) {
        String notFound = Arrays.stream(ids)
                .filter(id -> !films.containsKey(id))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        if (!notFound.isBlank()) {
            log.debug("Film with ID {} is not found", notFound);
            throw new NotFoundException("Film with ID " + notFound + " is not found");
        }
    }

}

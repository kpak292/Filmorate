package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.FilmDAO;
import ru.yandex.practicum.filmorate.entities.Film;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@Primary
public class DBFilmDAO extends BaseRepository<Film> implements FilmDAO {
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    private static final String FIND_ALL_QUERY = """
            SELECT *,
                   (SELECT Group_concat(genre_id)
                    FROM   genres g
                    WHERE  f.id = g.film_id
                           AND g.status_id = 0) AS genres
            FROM   films f
            WHERE  f.status_id = 0;""";

    private static final String FIND_BY_ID_QUERY = """
            SELECT *,
                   (SELECT Group_concat(genre_id)
                    FROM   genres g
                    WHERE  f.id = g.film_id
                           AND g.status_id = 0) AS genres
            FROM   films f
            WHERE  f.status_id = 0
                   AND f.id = ?;""";

    private static final String INSERT_QUERY = """
            INSERT INTO films
                        (name,
                         description,
                         release_date,
                         duration,
                         rating_id)
            VALUES      (?,
                         ?,
                         ?,
                         ?,
                         ?)""";



    private static final String UPDATE_FILM_QUERY = """
            UPDATE films
            SET    name = ?,
                   description = ?,
                   release_date = ?,
                   rating_id = ?
            WHERE  id = ?""";

    private static final String DELETE_QUERY = """
            UPDATE films
            SET    status_id = 1
            WHERE  id = ?""";



    public DBFilmDAO(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(
                        () -> new NotFoundException("Film is not found with id " + id)
                );
    }

    @Override
    public Film create(Film film) {
        validate(film);

        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getGenres(),
                film.getMpa());
        return film;
    }


    @Override
    public Film delete(long id) {
        Film film = getById(id);
        update(DELETE_QUERY, id);
        return film;
    }

    @Override
    public Map<Film, Integer> addLike(long filmId, long userId) {
        return Map.of();
    }

    @Override
    public Map<Film, Integer> removeLike(long filmId, long userId) {
        return Map.of();
    }

    @Override
    public Collection<Film> getTop(int count) {
        return List.of();
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

}

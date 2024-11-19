package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.entities.Film;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Repository
@Primary
public class DBFilmRepository extends BaseRepository<Film> implements FilmRepository {
    @Autowired
    DBLikeRepository likeRepository;

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

    private static final String UPDATE_QUERY = """
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

    private static final String ADD_LIKE_QUERY = """
            INSERT INTO likes
                        (user_id,
                         film_id)
            VALUES      (?,
                         ?)""";

    private static final String DELETE_LIKE_QUERY = """
            UPDATE likes
            SET    status_id = 1
            WHERE  user_id = ?
                   AND film_id = ?""";

    private static final String FIND_TOP_LIKED_QUERY = """
            SELECT f.*,
                   Count(l.user_id) AS count,
                   (SELECT Group_concat(genre_id)
                    FROM   genres g
                    WHERE  f.id = g.film_id
                           AND g.status_id = 0) AS genres
            FROM   films f
                   INNER JOIN likes l
                           ON f.id = l.film_id
            WHERE  f.status_id = 0
                   AND l.status_id = 0
            GROUP  BY f.id
            ORDER  BY count DESC
            LIMIT 10;""";

    private static final String DELETE_ALL_LIKES_QUERY = """
            UPDATE likes
            SET    status_id = 1
            WHERE  film_id = ?""";


    public DBFilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> findAll() {
        log.debug("FilmDAO/getAll");

        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film getById(long id) {
        log.debug("FilmDAO/getById - Film ID: {}", id);

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

        log.debug("FilmDAO/create - Added film: {}", film.toString());
        return film;
    }

    @Override
    public Film update(Film film) {
        validate(film);

        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());

        log.debug("FilmDAO/update - Updated film: {}", film.toString());
        return film;
    }

    @Override
    public Film delete(long id) {
        Film film = getById(id);
        update(DELETE_QUERY, id);
        removeAllLikes(id);

        log.debug("FilmDAO/delete - removed Film ID: {}", id);
        return film;
    }

    @Override
    public Map<Film, Integer> addLike(long filmId, long userId) {
        insert(ADD_LIKE_QUERY, userId, filmId);
        Film film = getById(filmId);

        int amount = likeRepository.getById(filmId).get(filmId);

        log.debug("FilmDAO/addLike - added like for Film ID {} from user ID {}", filmId, userId);
        return Map.of(film, amount);
    }

    @Override
    public Map<Film, Integer> removeLike(long filmId, long userId) {
        update(DELETE_LIKE_QUERY, userId, filmId);
        Film film = getById(filmId);

        int amount = likeRepository.getById(filmId).get(filmId);

        log.debug("FilmDAO/removeLike - removed like for Film ID {} from user ID {}", filmId, userId);
        return Map.of(film, amount);
    }

    @Override
    public Collection<Film> getTop(int count) {
        log.debug("FilmDAO/getTop");

        return findMany(FIND_TOP_LIKED_QUERY);
    }

    @Override
    public void validate(Film film) {
        if (film.getId() > 0) {
            findOne(FIND_BY_ID_QUERY, film.getId()).orElseThrow(
                    () -> new NotFoundException("Film is not found with id " + film.getId())
            );
        } else if (film.getId() < 0) {
            throw new ValidationException("Film:id - id cannot be negative");
        }

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

    public void removeAllLikes(long filmId){
        update(DELETE_ALL_LIKES_QUERY, filmId);
    }

}

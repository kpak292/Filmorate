package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.entities.Genre;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.Collection;

@Slf4j
@Repository
@Primary
public class DBGenreRepository extends BaseRepository<Genre> {
    @Autowired
    public DBGenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM genre""";

    private static final String INSERT_QUERY = """
            INSERT INTO genres
                        (film_id,
                         genre_id)
            VALUES      (?,
                         ?)""";

    private static final String FIND_BY_FILM_ID_QUERY = """
            SELECT *
            FROM   genre gen
                   LEFT JOIN genres gens
                          ON gen.id = gens.genre_id
            WHERE  gens.film_id = ?
                   AND gens.status_id = 0""";

    private static final String DELETE_QUERY = """
            UPDATE genres
            SET    status_id = 1
            WHERE  film_id = ?
                   AND genre_id = ?""";

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM   genre
            WHERE  id = ?""";

    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public long create(long filmId, int genreId) {
        return insert(INSERT_QUERY, filmId, genreId);
    }

    public Collection<Genre> getGenresByFilmId(long id) {
        return findMany(FIND_BY_FILM_ID_QUERY, id);
    }

    public long delete(long filmId, int genreId) {
        update(DELETE_QUERY, filmId, genreId);
        return genreId;
    }

    public Genre getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(
                        () -> new NotFoundException("Genre is not found with id " + id)
                );
    }
}

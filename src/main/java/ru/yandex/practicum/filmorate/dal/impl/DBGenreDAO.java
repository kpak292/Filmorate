package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.entities.Genre;

import java.util.Collection;

@Slf4j
@Repository
@Primary
public class DBGenreDAO extends BaseRepository<Genre> {
    public DBGenreDAO(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
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

    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public long create(long filmId, int genreId) {
        return insert(INSERT_QUERY, filmId, genreId);

    }
}

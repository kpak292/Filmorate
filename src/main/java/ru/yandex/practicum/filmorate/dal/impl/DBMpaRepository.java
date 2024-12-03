package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.entities.Mpa;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.Collection;

@Slf4j
@Repository
@Primary
public class DBMpaRepository extends BaseRepository<Mpa> {
    @Autowired
    public DBMpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM rating""";

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM   rating
            WHERE  id = ?""";

    public Collection<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Mpa getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElseThrow(
                        () -> new NotFoundException("MPA is not found with id " + id)
                );
    }

}

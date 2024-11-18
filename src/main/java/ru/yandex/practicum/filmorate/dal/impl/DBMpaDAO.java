package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.entities.Mpa;

import java.util.Collection;

@Slf4j
@Repository
@Primary
public class DBMpaDAO extends BaseRepository<Mpa> {
    public DBMpaDAO(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM rating""";

    public Collection<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
}

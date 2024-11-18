package ru.yandex.practicum.filmorate.dal.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Repository
@Primary
public class DBLikeDAO extends BaseRepository<Map<Long,Integer>> {
    public DBLikeDAO(JdbcTemplate jdbc, RowMapper<Map<Long,Integer>> mapper) {
        super(jdbc, mapper);
    }

    private static final String FIND_BY_ID_QUERY = """
            SELECT film_id,
                   Count(user_id) AS count
            FROM   likes
            WHERE  film_id = ?
                   AND status_id = 0
            GROUP  BY film_id""";

    public Map<Long,Integer> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id)
                .orElse(
                        Map.of(id, 0)
                );
    }

}

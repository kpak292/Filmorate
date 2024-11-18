package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.entities.Film;
import ru.yandex.practicum.filmorate.entities.Genre;
import ru.yandex.practicum.filmorate.entities.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("rating_id"));

        List<Genre> genres = Arrays.stream(rs.getString("genres").split(","))
                .mapToInt(Integer::parseInt)
                .mapToObj(id -> {
                    Genre genre =  new Genre();
                    genre.setId(id);
                    return genre;
                })
                .toList();


        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .genres(genres)
                .build();
    }
}

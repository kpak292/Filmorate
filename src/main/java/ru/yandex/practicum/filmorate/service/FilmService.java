package ru.yandex.practicum.filmorate.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDAO;
import ru.yandex.practicum.filmorate.dal.UserDAO;
import ru.yandex.practicum.filmorate.dal.impl.DBGenreDAO;
import ru.yandex.practicum.filmorate.dal.impl.DBMpaDAO;
import ru.yandex.practicum.filmorate.entities.Film;
import ru.yandex.practicum.filmorate.entities.Genre;
import ru.yandex.practicum.filmorate.entities.Mpa;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmDAO filmRepository;
    private final DBGenreDAO genreRepository;
    private final DBMpaDAO mpaRepository;
    private final UserDAO userRepository;

    public Collection<Film> findAll() {
        return filmRepository.findAll();
    }

    public Film create(Film film) {
        //populate genres and MPA with names
        Film filmWithNames = validateMpaAndGenre(film);

        //record film
        Film result = filmRepository.create(filmWithNames);


        //record links of films and genres
        result.getGenres().forEach(
                genre -> genreRepository.create(film.getId(), genre.getId()));

        return result;
    }

    public Film update(Film film) {
        //populate genres and MPA with names
        Film filmWithNames = validateMpaAndGenre(film);

        Film result = filmRepository.update(film);

        Collection<Genre> filmGenres = genreRepository.getGenresByFilmId(result.getId());

        filmGenres.stream()
                .filter(genre -> !result.getGenres().contains(genre))
                .forEach(genre -> genreRepository.create(result.getId(), genre.getId()));

        result.getGenres().stream()
                .filter(genre -> !filmGenres.contains(genre))
                .forEach(genre -> genreRepository.delete(result.getId(), genre.getId()));

        return result;
    }

    public Film delete(long id) {
        return filmRepository.delete(id);
    }

    public Film getById(long id) {
        return validateMpaAndGenre(filmRepository.getById(id));
    }

    public Map<Film, Integer> addLike(long filmId, long userId) {
        userRepository.getById(userId);
        filmRepository.getById(filmId);

        Map<Film, Integer> result =  filmRepository.addLike(filmId, userId);
        result.keySet().forEach(this::validateMpaAndGenre);

        return result;
    }

    public Map<Film, Integer> removeLike(long filmId, long userId) {
        userRepository.getById(userId);
        filmRepository.getById(filmId);

        Map<Film, Integer> result =  filmRepository.removeLike(filmId, userId);
        result.keySet().forEach(this::validateMpaAndGenre);

        return result;
    }

    public Collection<Film> getTop(int count) {
        return filmRepository.getTop(count);
    }

    private Film validateMpaAndGenre(Film film) {
        Collection<Genre> genres = genreRepository.findAll();

        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }

        film.setGenres(film.getGenres().stream()
                .distinct()
                .toList());

        film.getGenres()
                .forEach(genre -> {
            Genre genreWithName = genres.stream()
                    .filter(g -> g.getId() == genre.getId())
                    .findFirst()
                    .orElseThrow(
                            () -> new ValidationException("Invalid Genre with id " + genre.getId())
                    );

            genre.setName(genreWithName.getName());
        });

        //Get all ratings
        Collection<Mpa> mpas = mpaRepository.findAll();

        film.getMpa().setName(
                mpas.stream()
                        .filter(mpa -> mpa.getId() == film.getMpa().getId())
                        .findFirst()
                        .orElseThrow(
                                () -> new ValidationException("Invalid MPA with id " + film.getMpa().getId())
                        ).getName()
        );

        return film;
    }

}

package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.impl.DBGenreDAO;
import ru.yandex.practicum.filmorate.dal.impl.DBMpaDAO;
import ru.yandex.practicum.filmorate.entities.Genre;
import ru.yandex.practicum.filmorate.entities.Mpa;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class GenresService {
    private final DBGenreDAO repository;

    public Collection<Genre> getAll() {
        return repository.findAll();
    }

    public Genre getById(long id) {
        return repository.getById(id);
    }
}

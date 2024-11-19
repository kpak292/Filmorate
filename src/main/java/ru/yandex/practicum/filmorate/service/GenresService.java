package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.impl.DBGenreRepository;
import ru.yandex.practicum.filmorate.entities.Genre;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class GenresService {
    @Autowired
    private final DBGenreRepository repository;

    public Collection<Genre> getAll() {
        return repository.findAll();
    }

    public Genre getById(long id) {
        return repository.getById(id);
    }
}

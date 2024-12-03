package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.impl.DBMpaRepository;
import ru.yandex.practicum.filmorate.entities.Mpa;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class MpaService {
    @Autowired
    private final DBMpaRepository repository;

    public Collection<Mpa> getAll() {
        return repository.findAll();
    }

    public Mpa getById(long id) {
        return repository.getById(id);
    }
}

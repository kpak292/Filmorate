package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserDAO;
import ru.yandex.practicum.filmorate.dal.impl.DBMpaDAO;
import ru.yandex.practicum.filmorate.entities.Mpa;
import ru.yandex.practicum.filmorate.entities.User;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class MpaService {
    private final DBMpaDAO repository;

    public Collection<Mpa> getAll() {
        return repository.findAll();
    }

    public Mpa getById(long id) {
        return repository.getById(id);
    }
}

package ru.yandex.practicum.filmorate.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class Genre {
    private int id;
    private String name;
}

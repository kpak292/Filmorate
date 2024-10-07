package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    int id;
    String name;
    String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;
    int duration = 0;
}

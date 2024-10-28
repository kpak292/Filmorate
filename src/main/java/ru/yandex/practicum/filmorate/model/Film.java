package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.commons.RatingMPA;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;
    int duration;
    String genre;
    RatingMPA ratingMPA;
}

package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Film {
    int id;
    @NonNull
    @NotBlank(message = "Name cannot be null or blank")
    String name;
    @Size(max = 200, message = "Description cannot be more than 200 symbols")
    String description;
    @IsAfter(current = "1985-12-28", message = "Release date should be after 1985-12-28")
    @Past(message = "Release data cannot be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;
    @PositiveOrZero(message = "Duration should be positive")
    int duration = 0;
}

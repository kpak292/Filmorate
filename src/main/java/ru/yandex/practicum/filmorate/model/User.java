package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long id;
    @Email(message = "Email format is incorrect")
    @NotBlank
    String email;
    @NotBlank
    String login;
    String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}

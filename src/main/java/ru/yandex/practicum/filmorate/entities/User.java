package ru.yandex.practicum.filmorate.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[A-Za-z0-9]{1,200}$",
            message = "username must be of 6 to 12 length with no special characters")
    String login;
    String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}

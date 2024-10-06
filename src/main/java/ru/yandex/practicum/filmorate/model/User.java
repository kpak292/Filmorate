package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class User {
    int id;
    @NotBlank(message = "Email cannot be null or blank")
    @Email(message = "Email format is incorrect")
    String email;
    @NotBlank(message = "Login format is incorrect")
    String login;
    String name;
    @Past(message = "Date of birth cannot be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}

package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class User {
    int id;
    @NonNull
    @NotBlank(message = "Email cannot be null or blank")
    @Email(message = "Email format is incorrect")
    String email;
    @NonNull
    @NotBlank(message = "Login format is incorrect")
    String login;
    String name;
    @Past(message = "Date of birth cannot be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}

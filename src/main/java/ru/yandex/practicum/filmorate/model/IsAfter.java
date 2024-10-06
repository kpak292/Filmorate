package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface IsAfter{
    String message() default "{message.key}";
    String current();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

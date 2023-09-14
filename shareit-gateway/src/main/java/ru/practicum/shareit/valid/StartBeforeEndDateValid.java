package main.java.ru.practicum.shareit.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CheckDateValidator.class)
@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)

public @interface StartBeforeEndDateValid {

    String message() default "Дата начала бронирования не может быть пустой или быть позже даты его окончания!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
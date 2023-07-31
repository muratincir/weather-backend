package com.folksdev.weather.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {CityNameValidator.class}) // citynamevalidator tarafından KISITLAMA yapılacak
@Target({METHOD,FIELD,PARAMETER})  // HEDEF ALINAN BÖLGELER
@Retention(RUNTIME) // BEKLETME YAPACAĞI ZAMAN (ÇALIŞMA ZAMANI)

public @interface CityNameConstraint {
    String message() default "Invalid city name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

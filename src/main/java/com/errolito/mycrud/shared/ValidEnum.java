package com.errolito.mycrud.shared;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
    Class<? extends Enum<?>> value();

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    String message() default "Invalid value. Must be one of the allowed constants.";
}
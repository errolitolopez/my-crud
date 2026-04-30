package com.errolito.mycrud.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartFileValidator.class)
public @interface ValidFile {
    String message() default "File is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
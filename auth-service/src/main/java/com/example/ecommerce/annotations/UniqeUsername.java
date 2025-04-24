package com.example.ecommerce.annotations;

import com.example.ecommerce.validators.UniqeUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqeUsernameValidator.class)
public @interface UniqeUsername {
    String message() default "Username already exists"; // default error message when validation fails
    Class<?>[] groups() default {}; // for grouping validations
    Class<? extends Payload>[] payload() default {}; // for carrying metadata
}

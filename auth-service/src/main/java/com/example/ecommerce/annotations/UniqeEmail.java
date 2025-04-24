package com.example.ecommerce.annotations;

import com.example.ecommerce.validators.UniqeEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD}) // This annotation can only be applied to fields
@Retention(RetentionPolicy.RUNTIME) // The annotation will be available at runtime through reflection
@Documented // This annotation will be included in the Javadoc
@Constraint(validatedBy = UniqeEmailValidator.class)
public @interface UniqeEmail {
    String message() default "Email already exists, please use another email"; // default error message when validation fails
    Class<?>[] groups() default {}; // for grouping validations
    Class<? extends Payload>[] payload() default {}; // for carrying metadata
}

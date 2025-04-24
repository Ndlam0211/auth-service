package com.example.ecommerce.validators;

import com.example.ecommerce.annotations.UniqeEmail;
import com.example.ecommerce.modules.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqeEmailValidator implements ConstraintValidator<UniqeEmail, String> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepo.existsByEmail(email); // return true if email does not exist in the database
    }
}

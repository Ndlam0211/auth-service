package com.example.ecommerce.validators;

import com.example.ecommerce.annotations.UniqeEmail;
import com.example.ecommerce.annotations.UniqeUsername;
import com.example.ecommerce.modules.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqeUsernameValidator implements ConstraintValidator<UniqeUsername, String> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        return !userRepo.existsByUserName(userName); // return true if email does not exist in the database
    }
}

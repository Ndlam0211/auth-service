package com.example.ecommerce.validators;

import com.example.ecommerce.annotations.UniqePhone;
import com.example.ecommerce.annotations.UniqeUsername;
import com.example.ecommerce.modules.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqePhoneValidator implements ConstraintValidator<UniqePhone, String> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return !userRepo.existsByPhone(phone); // return true if phone does not exist in the database
    }
}

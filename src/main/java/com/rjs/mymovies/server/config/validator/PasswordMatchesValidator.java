package com.rjs.mymovies.server.config.validator;

import com.rjs.mymovies.server.config.annotation.PasswordMatches;
import com.rjs.mymovies.server.model.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    public PasswordMatchesValidator() {
    }

    @Override
    public void initialize(PasswordMatches passwordMatches) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        UserDto userDto = (UserDto) obj;

        return userDto.getPassword().equals(userDto.getConfirmPassword());
    }
}

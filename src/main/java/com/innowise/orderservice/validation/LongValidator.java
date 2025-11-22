package com.innowise.orderservice.validation;

import com.innowise.orderservice.validation.annotation.LongValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LongValidator implements ConstraintValidator<LongValid, String> {

  @Override
  public boolean isValid(String contactField, ConstraintValidatorContext constraintValidatorContext) {
    try {
      Long.parseLong(contactField);
    }catch (Exception e){
      return false;
    }
    return true;
  }
}

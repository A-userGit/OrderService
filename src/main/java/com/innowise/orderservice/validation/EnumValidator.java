package com.innowise.orderservice.validation;

import com.innowise.orderservice.validation.annotation.EnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {

  private Set<String> allowedValues;

  @Override
  public void initialize(EnumValid constraintAnnotation) {
    allowedValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(String testValue, ConstraintValidatorContext constraintValidatorContext) {
    return testValue != null && !testValue.isBlank() && allowedValues.contains(
        testValue.toUpperCase());
  }
}

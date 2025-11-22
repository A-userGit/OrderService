package com.innowise.orderservice.exception.handler;

import com.innowise.orderservice.exception.FeignClientException;
import com.innowise.orderservice.exception.ObjectAlreadyExistsException;
import com.innowise.orderservice.exception.ObjectNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@RestControllerAdvice
public class ErrorHandlerController {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(final
  MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage()));
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      final ConstraintViolationException e) {
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
      errors.put(constraintViolation.getRootBeanClass().getSimpleName() + "."
          + constraintViolation.getPropertyPath(), e.getMessage());
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<String> handleObjectNotFoundException(final ObjectNotFoundException e) {
    return ResponseEntity.notFound().header("error", e.getMessage()).build();
  }

  @ExceptionHandler(ObjectAlreadyExistsException.class)
  public ResponseEntity<String> handleObjectAlreadyExistsException(
      final ObjectAlreadyExistsException e) {
    return ResponseEntity.badRequest().header("error", e.getMessage()).build();
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<String> handleSQLException(final SQLException e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler(FeignClientException.class)
  public ResponseEntity<String> handleFeignClientException(final FeignClientException e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<String> handleEmptyResultDataAccessException(
      final EmptyResultDataAccessException e) {
    return ResponseEntity.unprocessableEntity().body(e.getMessage());
  }

  @ExceptionHandler(Forbidden.class)
  public ResponseEntity<String> handleForbiddenException(
      final EmptyResultDataAccessException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Unauthorized.class)
  public ResponseEntity<String> handleUnauthorizedException(
      final EmptyResultDataAccessException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }
}

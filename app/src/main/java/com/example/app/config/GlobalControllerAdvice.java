package com.example.app.config;

import com.example.adapters.openfda.exception.OpenFDAException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, Object> response = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    response.put("timestamp", Instant.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("errors", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OpenFDAException.class)
  public ResponseEntity<Object> handleOpenFDAException(OpenFDAException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", Instant.now());
    response.put("status", ex.getStatus().value());
    response.put("error", ex.getMessage());

    return new ResponseEntity<>(response, ex.getStatus());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", Instant.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Validation failed");
    response.put(
        "violations",
        ex.getConstraintViolations().stream()
            .map(
                violation ->
                    Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "message", violation.getMessage()))
            .toList());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}

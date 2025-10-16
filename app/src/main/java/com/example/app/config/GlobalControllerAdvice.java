package com.example.app.config;

import com.example.adapters.openfda.exception.OpenFDAException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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
            .collect(Collectors.toList()));

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OpenFDAException.class)
  public ResponseEntity<Object> handleOpenFDAException(OpenFDAException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", Instant.now());
    // Map any upstream OpenFDA adapter failures to 502 Bad Gateway (do not expose upstream status)
    response.put("status", HttpStatus.BAD_GATEWAY.value());
    response.put("error", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", Instant.now());
    int status =
        ex.getStatusCode() != null ? ex.getStatusCode().value() : HttpStatus.BAD_REQUEST.value();
    response.put("status", status);
    response.put("error", ex.getReason() != null ? ex.getReason() : ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.valueOf(status));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", Instant.now());
    response.put("status", HttpStatus.BAD_GATEWAY.value());
    response.put("error", "Upstream or internal error: " + ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
  }
}

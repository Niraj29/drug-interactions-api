package com.example.app.config;

import com.example.adapters.openfda.exception.OpenFDAException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(OpenFDAException.class)
  public ResponseEntity<Object> handleOpenFDAException(OpenFDAException ex, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now());
    body.put("message", ex.getMessage());
    body.put("status", ex.getStatus().value());

    return new ResponseEntity<>(body, ex.getStatus());
  }
}

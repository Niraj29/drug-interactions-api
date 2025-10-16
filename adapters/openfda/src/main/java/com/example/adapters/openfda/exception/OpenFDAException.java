package com.example.adapters.openfda.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class OpenFDAException extends RuntimeException {
  private final HttpStatusCode status;

  public OpenFDAException(String message, HttpStatusCode status) {
    super(message);
    this.status = status;
  }

  public OpenFDAException(String message, HttpStatusCode status, Throwable cause) {
    super(message, cause);
    this.status = status;
  }
}

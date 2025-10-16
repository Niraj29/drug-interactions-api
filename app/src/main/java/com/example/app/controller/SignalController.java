package com.example.app.controller;

import com.example.domain.model.SignalResponse;
import com.example.domain.ports.SignalService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/signals")
@RequiredArgsConstructor
@Validated
public class SignalController {
  private final SignalService signalService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<SignalResponse> getSignals(
      @NotBlank(message = "drugA is required") @RequestParam String drugA,
      @NotBlank(message = "drugB is required") @RequestParam String drugB,
      @RequestParam(required = false) @Min(value = 1, message = "limit must be at least 1")
          Integer limit) {
    return signalService
        .getSignals(drugA, drugB, limit)
        .onErrorResume(
            error ->
                Mono.error(new RuntimeException("Failed to fetch signals: " + error.getMessage())));
  }
}

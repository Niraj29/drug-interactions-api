package com.example.app.controller;

import com.example.app.dto.SignalsResponse;
import com.example.app.mapper.ApiMapper;
import com.example.domain.ports.SignalService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/signals")
@Validated
public class SignalController {
  private final SignalService signalService;
  private final ApiMapper mapper;

  public SignalController(SignalService signalService, ApiMapper mapper) {
    this.signalService = signalService;
    this.mapper = mapper;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<SignalsResponse> getSignals(
      @NotBlank(message = "drugA is required") @RequestParam String drugA,
      @NotBlank(message = "drugB is required") @RequestParam String drugB,
      @RequestParam(required = false)
          @Min(value = 1, message = "limit must be at least 1")
          @Max(value = 100, message = "limit must be at most 100")
          Integer limit) {
    return signalService.getSignals(drugA, drugB, limit).map(mapper::toSignalsResponse);
  }
}

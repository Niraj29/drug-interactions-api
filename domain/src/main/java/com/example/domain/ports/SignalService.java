package com.example.domain.ports;

import com.example.domain.model.SignalResponse;
import reactor.core.publisher.Mono;

public interface SignalService {
  Mono<SignalResponse> getSignals(String drugA, String drugB, Integer limit);
}

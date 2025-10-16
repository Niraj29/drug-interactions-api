package com.example.domain.ports;

import com.example.domain.model.OpenFdaSignal;
import reactor.core.publisher.Mono;

public interface OpenFdaClient {
  Mono<OpenFdaSignal> fetchSignals(String drugA, String drugB, int limit);
}

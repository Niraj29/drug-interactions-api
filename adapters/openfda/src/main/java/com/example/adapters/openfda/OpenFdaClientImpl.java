package com.example.adapters.openfda;

import com.example.adapters.openfda.exception.OpenFDAException;
import com.example.adapters.openfda.model.OpenFDACountResponse;
import com.example.domain.model.OpenFdaSignal;
import com.example.domain.ports.OpenFdaClient;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OpenFdaClientImpl implements OpenFdaClient {
  private final WebClient openFDAWebClient;

  @Override
  public Mono<OpenFdaSignal> fetchSignals(String drugA, String drugB, int limit) {
    String search = buildSearchQuery(drugA, drugB);

    return openFDAWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .queryParam("search", search)
                    .queryParam("count", "patient.reaction.reactionmeddrapt.exact")
                    .queryParam("limit", limit)
                    .build())
        .retrieve()
        .bodyToMono(OpenFDACountResponse.class)
        .map(this::toDomain)
        .onErrorMap(
            error -> {
              if (error instanceof OpenFDAException) return error;
              return new OpenFDAException(
                  "Failed to fetch signals from OpenFDA: " + error.getMessage(),
                  HttpStatus.BAD_GATEWAY);
            });
  }

  private String buildSearchQuery(String drugA, String drugB) {
    String a = quoteForSearch(drugA);
    String b = quoteForSearch(drugB);
    return String.format(
        "(patient.drug.medicinalproduct:\"%s\")+AND+(patient.drug.medicinalproduct:\"%s\")", a, b);
  }

  private String quoteForSearch(String raw) {
    if (raw == null) return "";
    // Escape internal quotes for the OpenFDA query; do not URL-encode here (WebClient will handle
    // encoding)
    return raw.replace("\"", "\\\"");
  }

  private OpenFdaSignal toDomain(OpenFDACountResponse resp) {
    if (resp == null || resp.getResults() == null) {
      return new OpenFdaSignal(0L, Collections.emptyList());
    }
    List<Map.Entry<String, Long>> entries =
        resp.getResults().stream()
            .map(r -> new AbstractMap.SimpleEntry<>(r.getTerm(), r.getCount()))
            .collect(Collectors.toList());
    long total = entries.stream().mapToLong(Map.Entry::getValue).sum();
    return new OpenFdaSignal(total, entries);
  }
}

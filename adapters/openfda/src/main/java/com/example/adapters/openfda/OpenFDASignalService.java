package com.example.adapters.openfda;

import com.example.adapters.openfda.config.OpenFDAProperties;
import com.example.adapters.openfda.model.OpenFDAResponse;
import com.example.domain.model.SignalResponse;
import com.example.domain.model.SignalResponse.AdverseEvent;
import com.example.domain.ports.SignalService;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OpenFDASignalService implements SignalService {
  private final WebClient openFDAWebClient;
  private final OpenFDAProperties properties;

  @Override
  public Mono<SignalResponse> getSignals(String drugA, String drugB, Integer limit) {
    return openFDAWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .queryParam("search", buildSearchQuery(drugA, drugB))
                    .queryParam("limit", limit != null ? limit : properties.getDefaultLimit())
                    .build())
        .retrieve()
        .bodyToMono(OpenFDAResponse.class)
        .map(response -> analyzeSignals(response, drugA, drugB));
  }

  private String buildSearchQuery(String drugA, String drugB) {
    return String.format(
        "(patient.drug.medicinalproduct:\"%s\")+AND+" + "(patient.drug.medicinalproduct:\"%s\")",
        drugA, drugB);
  }

  private SignalResponse analyzeSignals(OpenFDAResponse response, String drugA, String drugB) {
    Map<String, Long> reactionCounts =
        response.getResults().stream()
            .flatMap(event -> event.getPatient_reaction().stream())
            .collect(
                Collectors.groupingBy(
                    OpenFDAResponse.Reaction::getReactionmeddrapt, Collectors.counting()));

    double totalReports = response.getMeta().getResults().getTotal();

    var events =
        reactionCounts.entrySet().stream()
            .map(
                entry -> {
                  AdverseEvent event = new AdverseEvent();
                  event.setEffect(entry.getKey());
                  event.setCount(entry.getValue().intValue());
                  // Calculate PRR (Proportional Reporting Ratio)
                  double prr = calculatePRR(entry.getValue(), totalReports);
                  event.setPrr(prr);
                  // Calculate Chi-square statistic
                  event.setChi2(calculateChiSquare(entry.getValue(), totalReports));
                  return event;
                })
            .collect(Collectors.toList());

    SignalResponse signalResponse = new SignalResponse();
    signalResponse.setDrugA(drugA);
    signalResponse.setDrugB(drugB);
    signalResponse.setEvents(events);
    return signalResponse;
  }

  private double calculatePRR(double eventCount, double totalReports) {
    // PRR calculation logic
    // PRR = (a/b)/(c/d) where:
    // a = reports with the reaction for the drug combination
    // b = total reports for the drug combination
    // c = reports with the reaction for all drugs
    // d = total reports for all drugs
    return (eventCount / totalReports);
  }

  private double calculateChiSquare(double observed, double expected) {
    // Chi-square calculation: (O-E)²/E
    double difference = observed - expected;
    return (difference * difference) / expected;
  }
}

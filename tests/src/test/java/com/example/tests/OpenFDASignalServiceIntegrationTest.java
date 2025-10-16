package com.example.tests;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.example.adapters.openfda.OpenFDASignalService;
import com.example.app.DrugInteractionsApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest(classes = DrugInteractionsApplication.class)
@ActiveProfiles("test")
class OpenFDASignalServiceIntegrationTest {

  @Autowired private OpenFDASignalService signalService;

  @Autowired private WireMockServer wireMockServer;

  @BeforeEach
  void setUp() {
    wireMockServer.stubFor(
        get(urlPathMatching("/drug/event.json"))
            .withQueryParam("search", containing("Aspirin"))
            .withQueryParam("search", containing("Warfarin"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBodyFile("openfda/aspirin-warfarin-response.json")));
  }

  @Test
  void shouldFetchAndAnalyzeSignals() {
    StepVerifier.create(signalService.getSignals("Aspirin", "Warfarin", 10))
        .expectNextMatches(
            response -> {
              return "Aspirin".equals(response.getDrugA())
                  && "Warfarin".equals(response.getDrugB())
                  && !response.getEvents().isEmpty();
            })
        .verifyComplete();
  }
}

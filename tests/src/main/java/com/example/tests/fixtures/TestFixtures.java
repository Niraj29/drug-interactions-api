package com.example.tests.fixtures;

import com.example.domain.model.InteractionResponse;
import com.example.domain.model.InteractionUpsertRequest;

public class TestFixtures {
  public static InteractionUpsertRequest createTestRequest() {
    InteractionUpsertRequest request = new InteractionUpsertRequest();
    request.setDrugA("Aspirin");
    request.setDrugB("Warfarin");
    request.setNote("Increased risk of bleeding. Monitor closely.");
    return request;
  }

  public static InteractionResponse createTestResponse() {
    return InteractionResponse.builder()
        .drugA("Aspirin")
        .drugB("Warfarin")
        .note("Increased risk of bleeding. Monitor closely.")
        .build();
  }
}

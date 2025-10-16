package com.example.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignalResponse {
  private String drugA;
  private String drugB;
  private List<AdverseEvent> events;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AdverseEvent {
    private String effect;
    private int count;
    private double prr; // Proportional Reporting Ratio
    private double chi2; // Chi-square statistic
    private double confidence; // Statistical confidence level
  }
}

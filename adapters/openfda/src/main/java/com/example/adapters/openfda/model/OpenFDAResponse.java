package com.example.adapters.openfda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFDAResponse {
  private Meta meta;
  private List<Event> results;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Meta {
    private String disclaimer;
    private String terms;
    private String license;
    private Integer last_updated;
    private Results results;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Results {
    private Integer skip;
    private Integer limit;
    private Integer total;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Event {
    private String safetyreportid;
    private List<Drug> patient_drug;
    private List<Reaction> patient_reaction;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Drug {
    private String medicinalproduct;
    private String drugcharacterization;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Reaction {
    private String reactionmeddrapt;
    private String reactionoutcome;
  }
}

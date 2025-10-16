package com.example.adapters.openfda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFDACountResponse {
  private List<CountResult> results;

  public List<CountResult> getResults() {
    return results;
  }

  public void setResults(List<CountResult> results) {
    this.results = results;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class CountResult {
    private String term;
    private long count;

    public String getTerm() {
      return term;
    }

    public void setTerm(String term) {
      this.term = term;
    }

    public long getCount() {
      return count;
    }

    public void setCount(long count) {
      this.count = count;
    }
  }
}

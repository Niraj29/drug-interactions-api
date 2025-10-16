package com.example.app.dto;

import java.util.List;

public class SignalsResponse {
  private String drugA;
  private String drugB;
  private long count;
  private List<ReactionCount> topReactions;

  public SignalsResponse() {}

  public SignalsResponse(String drugA, String drugB, long count, List<ReactionCount> topReactions) {
    this.drugA = drugA;
    this.drugB = drugB;
    this.count = count;
    this.topReactions = topReactions;
  }

  public String getDrugA() {
    return drugA;
  }

  public void setDrugA(String drugA) {
    this.drugA = drugA;
  }

  public String getDrugB() {
    return drugB;
  }

  public void setDrugB(String drugB) {
    this.drugB = drugB;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public List<ReactionCount> getTopReactions() {
    return topReactions;
  }

  public void setTopReactions(List<ReactionCount> topReactions) {
    this.topReactions = topReactions;
  }
}

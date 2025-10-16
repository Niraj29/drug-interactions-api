package com.example.app.dto;

import java.time.Instant;

public class InteractionResponse {
  private String drugA;
  private String drugB;
  private String note;
  private Instant timestamp;

  public InteractionResponse() {}

  public InteractionResponse(String drugA, String drugB, String note, Instant timestamp) {
    this.drugA = drugA;
    this.drugB = drugB;
    this.note = note;
    this.timestamp = timestamp;
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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}

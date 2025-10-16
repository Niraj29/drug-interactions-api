package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class InteractionUpsertRequest {
  private static final String DRUG_NAME_PATTERN = "^[A-Za-z][A-Za-z\\s\\-]{1,58}[A-Za-z]$";

  @NotBlank(message = "drugA is required")
  @Size(min = 3, max = 60, message = "drugA must be between 3 and 60 characters")
  @Pattern(regexp = DRUG_NAME_PATTERN, message = "drugA contains invalid characters or format")
  private String drugA;

  @NotBlank(message = "drugB is required")
  @Size(min = 3, max = 60, message = "drugB must be between 3 and 60 characters")
  @Pattern(regexp = DRUG_NAME_PATTERN, message = "drugB contains invalid characters or format")
  private String drugB;

  @NotBlank(message = "note is required")
  @Size(min = 1, max = 1000, message = "note must be between 1 and 1000 characters")
  private String note;

  public InteractionUpsertRequest() {}

  public InteractionUpsertRequest(String drugA, String drugB, String note) {
    this.drugA = drugA;
    this.drugB = drugB;
    this.note = note;
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
}

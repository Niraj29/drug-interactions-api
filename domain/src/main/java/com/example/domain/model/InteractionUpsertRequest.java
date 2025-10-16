package com.example.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InteractionUpsertRequest {
  @NotBlank(message = "drugA is required")
  @Pattern(regexp = "^[\\w\\-. ]+$", message = "drugA contains invalid characters")
  @Size(min = 2, max = 100, message = "drugA must be between 2 and 100 characters")
  private String drugA;

  @NotBlank(message = "drugB is required")
  @Pattern(regexp = "^[\\w\\-. ]+$", message = "drugB contains invalid characters")
  @Size(min = 2, max = 100, message = "drugB must be between 2 and 100 characters")
  private String drugB;

  @NotBlank(message = "note is required")
  @Size(min = 10, max = 1000, message = "note must be between 10 and 1000 characters")
  private String note;
}

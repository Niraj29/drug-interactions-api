package com.example.app.controller;

import com.example.app.dto.InteractionResponse;
import com.example.app.dto.InteractionUpsertRequest;
import com.example.app.mapper.ApiMapper;
import com.example.domain.model.InteractionNote;
import com.example.domain.service.InteractionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/interactions")
@Validated
public class InteractionController {
  private static final String DRUG_NAME_PATTERN = "^[A-Za-z][A-Za-z\\s\\-]{1,58}[A-Za-z]$";

  private final InteractionService interactionService;
  private final ApiMapper mapper;

  public InteractionController(InteractionService interactionService, ApiMapper mapper) {
    this.interactionService = interactionService;
    this.mapper = mapper;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<InteractionResponse> upsertInteraction(
      @Valid @RequestBody InteractionUpsertRequest request) {
    InteractionNote note =
        interactionService.upsertInteraction(
            request.getDrugA(), request.getDrugB(), request.getNote());
    InteractionResponse resp = mapper.toInteractionResponse(note);
    return ResponseEntity.ok(resp);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<InteractionResponse> getInteraction(
      @NotBlank(message = "drugA is required")
          @Size(min = 3, max = 60, message = "drugA must be between 3 and 60 characters")
          @Pattern(
              regexp = DRUG_NAME_PATTERN,
              message = "drugA contains invalid characters or format")
          @RequestParam
          String drugA,
      @NotBlank(message = "drugB is required")
          @Size(min = 3, max = 60, message = "drugB must be between 3 and 60 characters")
          @Pattern(
              regexp = DRUG_NAME_PATTERN,
              message = "drugB contains invalid characters or format")
          @RequestParam
          String drugB) {
    var opt = interactionService.findInteraction(drugA, drugB);
    return opt.map(mapper::toInteractionResponse)
        .map(ResponseEntity::ok)
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "No interaction note found for the specified drug pair"));
  }
}

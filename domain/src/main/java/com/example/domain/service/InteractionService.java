package com.example.domain.service;

import com.example.domain.model.DrugPair;
import com.example.domain.model.InteractionNote;
import com.example.domain.model.OpenFdaSignal;
import com.example.domain.ports.InteractionRepository;
import com.example.domain.ports.OpenFdaClient;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InteractionService {
  private final InteractionRepository interactionRepository;
  private final OpenFdaClient openFdaClient;

  // validation per API: min 3, max 60, letters spaces and hyphens, must start and end with a letter
  private static final Pattern DRUG_NAME_PATTERN =
      Pattern.compile("^[A-Za-z][A-Za-z\\s\\-]{1,58}[A-Za-z]$");

  private String normalizeAndValidateName(String raw, String paramName) {
    if (raw == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, paramName + " must be provided");
    }
    String trimmed = raw.trim().replaceAll("\\s+", " ");
    // Title-case each word and hyphen-separated segment
    StringBuilder sb = new StringBuilder();
    String[] words = trimmed.split(" ");
    for (int i = 0; i < words.length; i++) {
      if (i > 0) sb.append(' ');
      String word = words[i];
      String[] parts = word.split("-");
      for (int j = 0; j < parts.length; j++) {
        if (j > 0) sb.append('-');
        String part = parts[j];
        if (part.isEmpty()) continue;
        sb.append(Character.toUpperCase(part.charAt(0)));
        if (part.length() > 1) sb.append(part.substring(1).toLowerCase());
      }
    }
    String normalized = sb.toString();

    if (normalized.length() < 3 || normalized.length() > 60) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format("%s must be between 3 and 60 characters", paramName));
    }
    if (!DRUG_NAME_PATTERN.matcher(normalized).matches()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format("%s contains invalid characters or format: %s", paramName, raw));
    }
    return normalized;
  }

  public InteractionNote upsertInteraction(String drugA, String drugB, String note) {
    String nA = normalizeAndValidateName(drugA, "drugA");
    String nB = normalizeAndValidateName(drugB, "drugB");

    DrugPair pair = new DrugPair(nA, nB);
    InteractionNote toUpsert = new InteractionNote(UUID.randomUUID(), pair, note, Instant.now());
    return interactionRepository.upsert(toUpsert);
  }

  public Optional<InteractionNote> findInteraction(String drugA, String drugB) {
    String nA = normalizeAndValidateName(drugA, "drugA");
    String nB = normalizeAndValidateName(drugB, "drugB");
    DrugPair pair = new DrugPair(nA, nB);
    return interactionRepository.find(pair);
  }

  public Mono<OpenFdaSignal> fetchSignals(String drugA, String drugB, int limit) {
    String nA = normalizeAndValidateName(drugA, "drugA");
    String nB = normalizeAndValidateName(drugB, "drugB");
    if (limit < 1 || limit > 100) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit must be between 1 and 100");
    }
    return openFdaClient.fetchSignals(nA, nB, limit);
  }
}

package com.example.domain.model;

import java.time.Instant;
import java.util.UUID;

public record InteractionNote(UUID id, DrugPair pair, String note, Instant updatedAt) {}

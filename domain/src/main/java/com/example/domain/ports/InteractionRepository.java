package com.example.domain.ports;

import com.example.domain.model.DrugPair;
import com.example.domain.model.InteractionNote;
import java.util.Optional;

public interface InteractionRepository {
  Optional<InteractionNote> find(DrugPair pair);

  InteractionNote upsert(InteractionNote note);
}

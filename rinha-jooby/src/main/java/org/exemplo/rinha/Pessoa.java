package org.exemplo.rinha;

import com.github.f4b6a3.uuid.UuidCreator;
import io.avaje.jsonb.Json;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Json
public record Pessoa(UUID id, String nome, String apelido, LocalDate nascimento, Set<String> stack) {
    public Pessoa(String nome, String apelido, LocalDate nascimento, Set<String> stack) {
        this(UuidCreator.getTimeOrderedEpochPlus1(), nome, apelido, nascimento, stack);
    }
}

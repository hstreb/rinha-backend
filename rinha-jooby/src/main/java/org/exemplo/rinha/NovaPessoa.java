package org.exemplo.rinha;

import io.avaje.jsonb.Json;

import java.time.LocalDate;
import java.util.Set;

@Json
public record NovaPessoa(String nome, String apelido,  LocalDate nascimento, Set<String> stack) {
}

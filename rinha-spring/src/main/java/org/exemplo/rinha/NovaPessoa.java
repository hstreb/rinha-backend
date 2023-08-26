package org.exemplo.rinha;

import java.time.LocalDate;
import java.util.Set;

public record NovaPessoa(String nome, String apelido, LocalDate nascimento, Set<String> stack) {
}

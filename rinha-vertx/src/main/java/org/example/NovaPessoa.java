package org.example;

import java.time.LocalDate;
import java.util.Set;

public record NovaPessoa(String nome, String apelido,  LocalDate nascimento, Set<String> stack) {
}

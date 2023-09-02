package org.example;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PessoaRepository extends R2dbcRepository<Pessoa, UUID> {

    @Query("SELECT id, nome, apelido, nascimento, stack FROM pessoas WHERE termo LIKE '%'||:termo||'%' LIMIT 50")
    Flux<Pessoa> findAllBySearch(String termo);
}

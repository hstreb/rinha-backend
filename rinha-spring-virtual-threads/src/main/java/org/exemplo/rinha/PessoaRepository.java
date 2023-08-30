package org.exemplo.rinha;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, UUID> {

    @Query("SELECT id, nome, apelido, nascimento, stack FROM pessoas WHERE termo ILIKE '%'||:termo||'%' LIMIT 50")
    List<Pessoa> findAllBySearch(String termo);
}

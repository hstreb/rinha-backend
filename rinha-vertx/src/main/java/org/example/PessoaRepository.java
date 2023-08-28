package org.example;

import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PessoaRepository {
    public static final String SEPARADOR = "¨";
    private final PgPool client;

    public PessoaRepository(PgPool client) {
        this.client = client;
    }

    private static Function<Row, Pessoa> mapear() {
        return row -> new Pessoa(row.getUUID("id"),
            row.getString("nome"),
            row.getString("apelido"),
            row.getLocalDate("nascimento"),
            separar(row.getString("stack"))
        );
    }

    private static String unir(Set<String> stack) {
        return stack == null ? null : String.join(SEPARADOR, stack);
    }

    private static Set<String> separar(String stack) {
        return stack == null ? null : Arrays.stream(stack.split(SEPARADOR)).collect(Collectors.toSet());
    }

    public Future<UUID> criar(Pessoa pessoa) {
        return client.preparedQuery("""
                INSERT INTO pessoas (id, nome, apelido, nascimento, stack)
                VALUES ($1, $2, $3, $4, $5)
                """)
            .execute(Tuple.of(pessoa.id(),
                pessoa.nome(),
                pessoa.apelido(),
                pessoa.nascimento(),
                unir(pessoa.stack())))
            .map(v -> pessoa.id());
    }

    public Future<Pessoa> buscar(UUID id) {
        return client.preparedQuery("SELECT * FROM pessoas WHERE id=$1").execute(Tuple.of(id))
            .map(RowSet::iterator)
            .map(iterator -> {
                if (iterator.hasNext()) {
                    var row = iterator.next();
                    return mapear().apply(row);
                }
                throw new RuntimeException("Pessoa '%d' não encontrada".formatted(id));
            });
    }

    public Future<List<Pessoa>> pesquisar(String termo) {
        return client.preparedQuery("SELECT * FROM pessoas WHERE termo LIKE '%'||$1||'%' LIMIT 50").execute(Tuple.of(termo))
            .map(rows -> StreamSupport.stream(rows.spliterator(), false).map(mapear()).toList());
    }

    public Future<Long> contar() {
        return client.preparedQuery("SELECT count(*) from pessoas").execute()
            .map(rows -> rows.iterator().next().getLong(0));
    }
}

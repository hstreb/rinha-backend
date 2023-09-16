package org.exemplo.rinha;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PessoaRepository {
    private static final String SEPARADOR = "¨";
    private static final String SQL_INSERIR = """
            INSERT INTO pessoas (id, nome, apelido, nascimento, stack)
            VALUES (?, ?, ?, ?, ?)
            """;
    public static final String SQL_BUSCAR = "SELECT id, nome, apelido, nascimento, stack FROM pessoas WHERE id = ?";
    public static final String SQL_PESQUISA = "SELECT * FROM pessoas WHERE termo LIKE '%'||?||'%' LIMIT 50";

    private DataSource dataSource;

    public PessoaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void criar(Pessoa pessoa) {
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.prepareStatement(SQL_INSERIR)) {
                stmt.setObject(1, pessoa.id());
                stmt.setString(2, pessoa.nome());
                stmt.setString(3, pessoa.apelido());
                stmt.setDate(4, Date.valueOf(pessoa.nascimento()));
                stmt.setString(5, unir(pessoa.stack()));
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Pessoa> buscar(UUID id) {
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.prepareStatement(SQL_BUSCAR)) {
                stmt.setObject(1, id);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<Pessoa> pesquisar(String termo) {
        var resultados = new ArrayList<Pessoa>();
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.prepareStatement(SQL_PESQUISA)) {
                stmt.setString(1, termo);
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    resultados.add(mapear(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultados;
    }

    public Long contar() {
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.prepareStatement("SELECT count(*) from pessoas")) {
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0L;
    }

    private Pessoa mapear(ResultSet rs) throws SQLException {
        return new Pessoa(rs.getObject("id", UUID.class),
                rs.getString("nome"),
                rs.getString("apelido"),
                rs.getDate("nascimento").toLocalDate(),
                separar(rs.getString("stack")));
    }

    private String unir(Set<String> stack) {
        return stack == null ? null : String.join(SEPARADOR, stack);
    }

    private Set<String> separar(String stack) {
        return stack == null ? null : Arrays.stream(stack.split(SEPARADOR)).collect(Collectors.toSet());
    }
}

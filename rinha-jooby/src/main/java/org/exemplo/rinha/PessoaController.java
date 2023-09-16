package org.exemplo.rinha;

import io.jooby.Context;

import java.util.List;
import java.util.UUID;

public class PessoaController {

    private final PessoaRepository repository;

    public PessoaController(PessoaRepository repository) {
        this.repository = repository;
    }

    public String criar(Context ctx) {
        NovaPessoa novaPessoa;
        try {
            novaPessoa = ctx.body(NovaPessoa.class);
        } catch (Exception ex) {
            ctx.setResponseCode(400);
            return "";
        }
        if (verificarParametrosInvalidos(novaPessoa)) {
            ctx.setResponseCode(422);
            return "";
        }
        var pessoa = new Pessoa(novaPessoa.nome(), novaPessoa.apelido(), novaPessoa.nascimento(), novaPessoa.stack());
        try {
            repository.criar(pessoa);
        } catch (Exception ex) {
            if (ex.getMessage().contains("uk_apelido")) {
                ctx.setResponseCode(422);
                return "";
            }
        }
        ctx.setResponseHeader("Location", "%s/%s".formatted(ctx.getRequestURL(), pessoa.id()));
        ctx.setResponseCode(201);
        return "";
    }

    public Pessoa buscar(Context ctx) {
        var pessoa = repository.buscar(getId(ctx));
        if (pessoa.isEmpty()) {
            ctx.setResponseCode(404);
            return null;
        }
        return pessoa.get();
    }

    public List<Pessoa> pesquisar(Context ctx) {
        var termo = ctx.query("t").value();
        return repository.pesquisar(termo);
    }

    public Long contar(Context ctx) {
        return repository.contar();
    }

    private boolean verificarParametrosInvalidos(NovaPessoa pessoa) {
        var nome = pessoa.nome();
        var apelido = pessoa.apelido();
        var stack = pessoa.stack();
        return apelido == null || apelido.length() > 32 ||
                nome == null || nome.length() > 100 ||
                (stack != null && stack.stream().anyMatch(s -> s == null || s.length() > 32));
    }

    private UUID getId(Context context) {
        try {
            return UUID.fromString(context.path("id").value());
        } catch (Exception ex) {
            return null;
        }
    }
}

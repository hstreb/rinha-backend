package org.exemplo.rinha;

import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgException;

import java.util.UUID;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpHeaders.LOCATION;
import static io.vertx.core.json.Json.encode;
import static java.lang.String.valueOf;

public class PessoaHandler {

    private static final String APPLICATION_JSON = "application/json";
    private final PessoaRepository pessoaRepository;

    public PessoaHandler(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    private String getUri(RoutingContext context, UUID id) {
        return "%s://%s/pessoas/%s".formatted(context.request().scheme(), context.request().authority(), id.toString());
    }

    private boolean verificarParametrosInvalidos(NovaPessoa pessoa) {
        var nome = pessoa.nome();
        var apelido = pessoa.apelido();
        var stack = pessoa.stack();
        return apelido == null || apelido.isBlank() || apelido.length() > 32 ||
                nome == null || nome.isBlank() || nome.length() > 100 ||
                (stack != null && stack.stream().anyMatch(s -> s == null || s.isBlank() || s.length() > 32));
    }

    public void criar(RoutingContext context) {
        NovaPessoa novaPessoa;
        try {
            novaPessoa = context.body().asPojo(NovaPessoa.class);
        } catch (Exception ex) {
            context.response().setStatusCode(400).end();
            return;
        }
        if (verificarParametrosInvalidos(novaPessoa)) {
            context.response().setStatusCode(422).end();
            return;
        }
        var pessoa = new Pessoa(novaPessoa.nome(), novaPessoa.apelido(), novaPessoa.nascimento(), novaPessoa.stack());
        pessoaRepository.criar(pessoa)
                .onSuccess(id -> context.response().putHeader(LOCATION, getUri(context, id)).setStatusCode(201).end())
                .onFailure(ex -> {
                    if (ex instanceof PgException pgEx && pgEx.getErrorMessage().contains("uk_apelido")) {
                        context.response().setStatusCode(422).end();
                    }
                });
    }

    public void buscar(RoutingContext context) {
        var id = getId(context);
        pessoaRepository.buscar(id)
                .onSuccess(p -> context.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(encode(p)))
                .onFailure(ex -> context.response().setStatusCode(404).end());
    }

    private UUID getId(RoutingContext context) {
        try {
            return UUID.fromString(context.pathParam("id"));
        } catch (Exception ex) {
            return null;
        }
    }

    public void pesquisar(RoutingContext context) {
        var termo = context.queryParams().get("t");
        if (termo == null) {
            context.response().setStatusCode(400).end();
        } else {
            pessoaRepository.pesquisar(termo)
                    .onSuccess(p -> context.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(encode(p)))
                    .onFailure(context::fail);
        }
    }

    public void contar(RoutingContext context) {
        pessoaRepository.contar()
                .onSuccess(q -> context.response().putHeader(CONTENT_TYPE, APPLICATION_JSON).end(valueOf(q)))
                .onFailure(context::fail);
    }
}

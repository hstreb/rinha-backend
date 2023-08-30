package org.exemplo.rinha;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.*;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "8080"));

    static {
        var objectMapper = DatabindCodec.mapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
    }

    public static void main(String[] args) {
        var vertx = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));
        vertx.deployVerticle(App::new, new DeploymentOptions().setInstances(1));
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var pessoaHandler = getPessoaHandler();

        var router = Router.router(vertx);
        router.post("/pessoas")
            .consumes("application/json")
            .handler(BodyHandler.create())
            .handler(pessoaHandler::criar);
        router.get("/pessoas/:id").handler(pessoaHandler::buscar);
        router.get("/pessoas").handler(pessoaHandler::pesquisar);
        router.get("/contagem-pessoas").handler(pessoaHandler::contar);

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(httpPort)
            .onSuccess(ok -> {
                LOGGER.info("Aplicação rodando na porta {}", httpPort);
                startPromise.complete();
            })
            .onFailure(failure -> {
                LOGGER.error("Aplicacao falhou ao iniciar", failure);
                System.exit(1);
            });
    }

    private PessoaHandler getPessoaHandler() {
        var connectOptions = new PgConnectOptions()
            .setPort(5432)
            .setHost(System.getenv().getOrDefault("DB_HOST", "localhost"))
            .setDatabase(System.getenv().getOrDefault("DB_NAME", "rinha"))
            .setUser(System.getenv().getOrDefault("DB_USER", "rinha"))
            .setPassword(System.getenv().getOrDefault("DB_PASSWORD", "rinha123"));
        var pgPool = PgPool.pool(vertx, connectOptions, new PoolOptions());

        var pessoaRepository = new PessoaRepository(pgPool);
        return new PessoaHandler(pessoaRepository);
    }
}

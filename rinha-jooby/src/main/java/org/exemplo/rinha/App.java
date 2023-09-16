package org.exemplo.rinha;

import io.jooby.avaje.jsonb.AvajeJsonbModule;
import io.jooby.hikari.HikariModule;
import io.jooby.undertow.UndertowServer;

import javax.sql.DataSource;

import static io.jooby.Jooby.runApp;

public class App {

    public static void main(final String[] args) {
        runApp(args, app -> {
            app.install(new UndertowServer());
            app.install(new AvajeJsonbModule());
            app.install(new HikariModule());

            var repository = new PessoaRepository(app.require(DataSource.class));
            var controller = new PessoaController(repository);

            app.post("/pessoas", controller::criar);
            app.get("/pessoas/{id}", controller::buscar);
            app.get("/pessoas", controller::pesquisar);
            app.get("/contagem-pessoas", controller::contar);
        });
    }

}

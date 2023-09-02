package org.example;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping("/pessoas")
    public Mono<ResponseEntity<Void>> criar(@RequestBody NovaPessoa novaPessoa, UriComponentsBuilder builder) {
        if (verificarParametrosInvalidos(novaPessoa)) {
            return Mono.just(ResponseEntity.unprocessableEntity().build());
        }
        var pessoa = new Pessoa(novaPessoa.nome(), novaPessoa.apelido(), novaPessoa.nascimento(), novaPessoa.stack());
        return pessoaRepository.save(pessoa)
                .map(p -> builder.pathSegment("pessoas", "{id}").buildAndExpand(pessoa.getId().toString()).toUri())
                .map(uri -> ResponseEntity.created(uri).build());
    }

    @GetMapping("/pessoas/{id}")
    public Mono<ResponseEntity<Pessoa>> buscar(@PathVariable UUID id) {
        return pessoaRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/pessoas")
    public Flux<Pessoa> pesquisar(@RequestParam("t") String termo) {
        return pessoaRepository.findAllBySearch(termo);
    }

    @GetMapping("/contagem-pessoas")
    public Mono<Long> contar() {
        return pessoaRepository.count();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ResponseEntity<Void>> handlePostError(Exception ex) {
        return Mono.just(ResponseEntity.unprocessableEntity().build());
    }

    private boolean verificarParametrosInvalidos(NovaPessoa pessoa) {
        var nome = pessoa.nome();
        var apelido = pessoa.apelido();
        var stack = pessoa.stack();
        return apelido == null || apelido.isBlank() || apelido.length() > 32 ||
                nome == null || nome.isBlank() || nome.length() > 100 ||
                (stack != null && stack.stream().anyMatch(s -> s == null || s.isBlank() || s.length() > 32));
    }
}


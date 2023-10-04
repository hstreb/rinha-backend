package org.exemplo.rinha;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping("/pessoas")
    public ResponseEntity<Void> criar(@RequestBody NovaPessoa novaPessoa, UriComponentsBuilder builder) {
        if (verificarParametrosInvalidos(novaPessoa)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        var pessoa = new Pessoa(novaPessoa.nome(), novaPessoa.apelido(), novaPessoa.nascimento(), novaPessoa.stack());
        pessoaRepository.save(pessoa);
        var uri = builder.pathSegment("pessoas", "{id}").buildAndExpand(pessoa.getId().toString()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> buscar(@PathVariable UUID id) {
        return pessoaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/pessoas")
    public List<Pessoa> pesquisar(@RequestParam("t") String termo) {
        return pessoaRepository.findAllBySearch(termo);
    }

    @GetMapping("/contagem-pessoas")
    public Long contar() {
        return pessoaRepository.count();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handlePostError(Exception ex) {
        return ResponseEntity.unprocessableEntity().build();
    }

    private boolean verificarParametrosInvalidos(NovaPessoa pessoa) {
        var nome = pessoa.nome();
        var apelido = pessoa.apelido();
        var stack = pessoa.stack();
        return apelido == null || apelido.length() > 32 ||
                nome == null || nome.length() > 100 ||
                (stack != null && stack.stream().anyMatch(s -> s == null || s.length() > 32));
    }
}

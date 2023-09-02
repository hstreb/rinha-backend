package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Table("pessoas")
public class Pessoa implements Serializable {

    @Id
    private UUID id;
    @JsonIgnore
    @Version
    private Integer versao;
    private String nome;
    private String apelido;
    private LocalDate nascimento;
    @Transient
    private Set<String> stack;
    @JsonIgnore
    @Column("stack")
    private String stackComposta;

    public Pessoa() {
    }

    public Pessoa(String nome, String apelido, LocalDate nascimento, Set<String> stack) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.nome = nome;
        this.apelido = apelido;
        this.nascimento = nascimento;
        this.stack = stack;
        this.stackComposta = getStackComposta();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public Set<String> getStack() {
        return stack == null ? stackComposta == null ? null : Arrays.stream(stackComposta.split("¨")).collect(Collectors.toSet()) : stack;
    }

    public void setStack(Set<String> stack) {
        this.stack = stack;
    }

    @JsonIgnore
    private String getStackComposta() {
        this.stackComposta = this.stack == null ? null : String.join("¨", this.stack);
        return this.stackComposta;
    }

    public void setStackComposta(String stackComposta) {
        this.stackComposta = stackComposta;
    }
}

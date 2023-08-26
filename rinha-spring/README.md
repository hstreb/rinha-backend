# Rinha backend springão da massa, quem paga as contas 

Projeto para resolver o desafio da rinha de backend https://github.com/zanfranceschi/rinha-de-backend-2023-q3, usando:

- java 17
- spring boot 3.1.3
- postgresql 15.4

## Sobre

- Github: https://github.com/hstreb
- Twitter: https://twitter.com/humbertostreb

## Otimizações

- as configurações do postgresql e nginx inspiradas na solução do [viniciusfonseca/rinha-backend-rust](https://github.com/viniciusfonseca/rinha-backend-rust/)

## cosntruir

```shell
./gradlew build
docker build -t docker/rinha-spring:0.0.1 .
```

## rodar

```shell
docker compose up -d
```

## Resultados

1 - bateria de testes, a melhor execução: 35822 pessoas inseridas

![img.png](imgs/execucao-01.png)

2 - bateria de testes, ajuste na distribuição de CPU entre os container, 39855 pessoas inseridas

![img.png](imgs/execucao-02.png)
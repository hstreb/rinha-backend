# Rinha backend, usando vertx

Projeto para resolver o desafio da rinha de backend https://github.com/zanfranceschi/rinha-de-backend-2023-q3, usando:

- java 17
- vertx 4.4.5
- postgresql 15.4

## Sobre

- Github: https://github.com/hstreb
- Twitter: https://twitter.com/humbertostreb

## Otimizações

- as configurações do postgresql e nginx inspiradas na solução do [viniciusfonseca/rinha-backend-rust](https://github.com/viniciusfonseca/rinha-backend-rust/)

## construir

- jvm
  ```shell
  ./gradlew build
  docker build -t hstreb/rinha-vertx:0.0.1 .
  ```

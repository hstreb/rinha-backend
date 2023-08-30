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

## construir

- jvm
  ```shell
  ./gradlew build
  docker build -t hstreb/rinha-spring:0.0.1 .
  ```

- native

  - construir o jar

  ```shell
  ./gradlew build
  ```
    
  - rodar o jar com agente

  ```shell
  docker compose up -d
  sleep 5
    
  java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image/ -jar build/libs/rinha-spring-0.0.1.jar &
  pid=$!
  ```

  - rodar algumas chamadas

  ```shell
  url_pessoa=$(echo '{"nome":"Teste","apelido":"teste19","nascimento":"2000-01-01","stack":["java","scala"]}' | http --headers :8080/pessoas | awk -F': ' 'tolower($1)=="location" {print $2}')
  curl $url_pessoa
  echo '{"nome":"Teste","apelido":"teste2","nascimento":"2000-01-01","stack":["java","scala"]}' | http :8080/pessoas
  echo '{"nome":"Teste","apelido":"teste","nascimento":"2000-01-01","stack":["java","scala"]}' | http :8080/pessoas
  echo '{"nome":"Teste","apelido":"teste","nascimento":"2000-01-01","stack":["java","scala"]}' | http :8080/pessoas
  echo '{"apelido":"teste1","nascimento":"2000-01-01","stack":["java","scala"]}' | http :8080/pessoas
  echo '{"nome":"Teste1","apelido":"teste1","nascimento":"2000-21-01","stack":["java","scala"]}' | http :8080/pessoas
  http :8080/pessoas
  http :8080/pessoas t==java
  http :8080/contagem-pessoas
  ```
  
  - construir a imagem nativa

  ```shell
  ./gradlew bootBuildImage --imageName=hstreb/rinha-spring:0.0.1-native
  ```

## rodar

```shell
docker compose up -d
```

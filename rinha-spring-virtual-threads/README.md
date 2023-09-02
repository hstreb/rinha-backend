# rinha de backend

## construir e rodar

### versão jvm

Construir o projeto

```shell
./gradlew build
```

Contruir a imagem docker

```shell
docker build -t hstreb/rinha-spring-virtual-threads:0.0.1-jvm .
```

Rodar linha de comando

```shell
java -jar build/libs/rinha-spring-virtual-threads-0.0.1.jar
```

### versão native

Para a imagem nativa funcionar corretamente, é preciso rodar ao menos uma vez o .jar da aplicação (gerado no passo
anterior) com
o [agente do graalvm](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.advanced.using-the-tracing-agent)
e executar os testes na api. Esse passo já foi executado e os arquivos estão no diretório correto.

```shell
java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image/ -jar build/libs/rinha-spring-virtual-threads-0.0.1.jar
```

Construir o projeto e criar imagem docker

```shell
./gradlew bootBuildImage
```

### docker compose

- versão jvm

```shell
docker compose -f docker-compose-jvm.yml up -d
```

- versão native

```shell
docker compose -f docker-compose-native.yml up -d
```

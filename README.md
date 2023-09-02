# rinha de backend

Soluções para o desafio da rinha de backend https://github.com/zanfranceschi/rinha-de-backend-2023-q3.

## Stress test

Script gatling do [zanfranceschi/rinha-de-backend-2023-q3](https://github.com/zanfranceschi/rinha-de-backend-2023-q3/tree/main/stress-test), com os apontamentos locais.

### Rodar

```shell
cd stress-test
./run-test.sh
```

## Soluções

- [Spring-boot](rinha-spring)

- [Vertx](rinha-vertx)

- [Spring-boot-virtual-threads](rinha-spring-virtual-threads)

- [Spring-boot-webflux](rinha-spring-webflux)

## Resultados parciais

Alguns resultados conforme a evolução dos projetos [resultados](resultados.md).

## placar atual

### rodar scrits

```shell
./start.sh rinha-spring jvm
./start.sh rinha-spring native
./start.sh rinha-spring-virtual-threads jvm
./start.sh rinha-spring-webflux jvm
./start.sh rinha-vertx jvm
```

### hardware

```shell
sudo lshw -short
Caminho do hardware  Dispositivo  Classe         Descrição
============================================================
/0                                bus            03TXXV
/0/0                              memory         64KiB BIOS
/0/40                             memory         16GiB Memória do sistema
/0/40/0                           memory         16GiB SODIMM DDR4 Síncrono Unbuffered (Unregistered) 2400 MHz (0,4 ns)
/0/44                             memory         128KiB L1 cache
/0/45                             memory         512KiB L2 cache
/0/46                             memory         4MiB L3 cache
/0/47                             processor      Intel(R) Core(TM) i7-7500U CPU @ 2.70GHz
```

### classificação

| projeto | profile | contagem de pessoas | p99 geral |
| --- | --- | --- | --- |
| viniciusfonseca | default | 44933 | 15580 |
| rinha-spring-webflux | jvm | 44302 | 16382 |
| rinha-vertx | jvm | 44228 | 16047 |
| rinha-spring-virtual-threads | jvm | 43914 | 18503 |
| rinha-spring | jvm | 43185 | 18490 |
| rinha-spring | native | 39950 | 22186 |

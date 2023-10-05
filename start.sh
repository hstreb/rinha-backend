#!/bin/bash

# parametros:
#   1: projeto, diretórios de código do repositorio, ex: rinha-spring
#   2: profile, ex: jvm ou native
# para e remover o containers docker
# iniciar o docker compose
# rodar script de teste e coletar as métricas

WORKSPACE=$(pwd)/stress-test

projeto=${1}
[[ -z "$1" ]] && { echo "Parametro 1 (projeto) é obrigatório!" ; exit 1; }

profile=${2:-default}

nome_projeto=$(echo $projeto | sed "s/participantes\///g")

data=$(date "+%Y%m%d%H%M%S")

log="${WORKSPACE}/user-files/results/${nome_projeto}-${profile}-${data}.log"

touch $log
echo $log

cd $projeto

docker compose --profile $profile stop >> $log 2>&1
docker compose --profile $profile rm -f >> $log 2>&1

start=`date +%s.%N`
docker compose --profile $profile up -d >> $log 2>&1

while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:9999/contagem-pessoas)" != "200" ]]
do
    echo "aguardando aceite de requisições" >> $log 2>&1
    sleep 1
done
end=`date +%s.%N`

runtime=$( echo "$end - $start" | bc -l )
echo "[${projeto}]: aceitando requisições após: ${runtime} segundos"  >> $log 2>&1

qtde_inicial=$(curl -s "http://localhost:9999/contagem-pessoas")

echo "contagem inicial: ${qtde_inicial}" >> $log 2>&1

GATLING_BIN_DIR=$HOME/tools/gatling-3.9.5/bin

sh $GATLING_BIN_DIR/gatling.sh -rm local -s RinhaBackendSimulation \
    -rd "DESCRICAO" \
    -rf $WORKSPACE/user-files/results \
    -sf $WORKSPACE/user-files/simulations \
    -rsf $WORKSPACE/user-files/resources >> $log 2>&1

sleep 3

curl -v "http://localhost:9999/contagem-pessoas" >> $log 2>&1

pessoas=$(cat $log | tail -n 1 | awk '{print $NF}')

p99=$(grep 'response time 99th percentile' $log | awk '{print $6}')

echo "| ${nome_projeto} | ${profile} | ${pessoas} | ${p99} |"

docker compose --profile $profile stop > /dev/null 2>&1

cd ..

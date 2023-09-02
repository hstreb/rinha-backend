#!/bin/bash

# parar o docker compose

projeto=${1}
[[ -z "$1" ]] && { echo "Parametro 1 (projeto) é obrigatório!" ; exit 1; }

profile=${2:-jvm}

cd $projeto

docker compose --profile $profile stop > /dev/null 2>&1
docker compose --profile $profile rm -f > /dev/null 2>&1
#! /bin/bash

if [ -d "./jars/" ]
then
	rm -r ./jars/*
fi
docker build -t build-jar-wallet-provider-image ./providers/
docker_output=$(docker ps -a --format '{{.Names}}' | grep build-jar-wallet-provider)
if  [ -z "${docker_output}" ]
then
	docker create -it --name build-jar-wallet-provider build-jar-wallet-provider-image bash
fi
docker cp build-jar-wallet-provider:/home/app/target/builtinin-wallet-authenticator.jar ./jars/builtinin-wallet-authenticator.jar
docker cp build-jar-wallet-provider:/home/app/keycloak-database-federation/dist/. ./jars/
docker rm -f build-jar-wallet-provider
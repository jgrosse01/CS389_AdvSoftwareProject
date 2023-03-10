#!/bin/bash
while True; do echo $PWD; done

until ping -c 1 -W 1 db_primary
do
echo "Waiting for database to spin up"
sleep 3s
done
WORKDIR /usr/docker/application
./gradlew bootRun
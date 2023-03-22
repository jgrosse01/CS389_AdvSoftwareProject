#!/bin/sh
CONTAINER=resume-tracker
TAG=latest
JAR=cs389-0.0.1-SNAPSHOT.jar

cp ../../../build/libs/$JAR .

docker build -t $CONTAINER:$TAG .

rm -f $JAR

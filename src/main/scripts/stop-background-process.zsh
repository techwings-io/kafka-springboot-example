#!/bin/zsh

KAFKA_PID=$(ps -efa | grep kafka.Kafka | grep -v grep |awk '{ print $2 }')
echo "Will kill PID $KAFKA_PID"

kill -9 "$KAFKA_PID"
echo "Process killed"
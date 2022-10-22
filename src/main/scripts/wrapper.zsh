#!/bin/zsh

./start-kafka-server.zsh &
KAFKA_PID=$!
echo "Starting Kafka broker with PID: $KAFKA_PID"

echo "Waiting 30 seconds for the server to be up and running..."
sleep 30

./create-topic.zsh

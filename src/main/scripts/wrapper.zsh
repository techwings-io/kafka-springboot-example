#!/bin/zsh

./start-kafka-server.zsh &

echo "Waiting 30 seconds for the server to be up and running..."
sleep 30

./create-topic.zsh

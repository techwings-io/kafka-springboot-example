#!/bin/zsh

echo "Creating topic: first_topic"
kafka-topics --bootstrap-server localhost:9092 --create --topic first_topic >> /dev/null
echo "Topic: first_topic created"
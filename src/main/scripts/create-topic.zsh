#!/bin/zsh

echo "Creating topic: first_topic"
kafka-topics --bootstrap-server localhost:9092 --create --topic "first_topic" --partitions 3 >> /dev/null
echo "Topic: first_topic created"

echo "Creating topic: wikimedia_rc"
kafka-topics --bootstrap-server localhost:9092 --create --topic "wikimedia_rc" --partitions 3 >> /dev/null
echo "Topic: first_topic wikimedia_rc"

echo "Creating topic: wikimedia_rc_connect"
kafka-topics --bootstrap-server localhost:9092 --create --topic "wikimedia_rc_connect" --partitions 3 >> /dev/null
echo "Topic: first_topic wikimedia_rc_connect"
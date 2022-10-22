#!/bin/zsh
kraft_server_properties="$KAFKA_HOME/config/kraft/server.properties"
clusterId=$(kafka-storage random-uuid)


echo "$clusterId"


echo "Formatting cluster with id $clusterId ..."
kafka-storage format -t "$clusterId" -c "$kraft_server_properties" --ignore-formatted

echo "Starting broker..."
kafka-server-start "$kraft_server_properties" 

echo "Creating topic: first_topic"
kafka-topics --bootstrap-server localhost:9092 --create --topic first_topic >> /dev/null
echo "Topic: first_topic created"
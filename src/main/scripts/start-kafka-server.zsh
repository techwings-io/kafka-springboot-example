#!/bin/zsh
kraft_server_properties="$KAFKA_HOME/config/kraft/server.properties"
clusterId=$(kafka-storage random-uuid)


echo "$clusterId"


echo "Formatting cluster with id $clusterId ..."
kafka-storage format -t "$clusterId" -c "$kraft_server_properties" --ignore-formatted

echo "Starting server..."
kafka-server-start "$kraft_server_properties"
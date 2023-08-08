#!/bin/sh
# Creates directory which the connector will scan for new files
docker exec -it connect mkdir -p /tmp/kafka-connect/examples;
# Put the connector using the configuration in the JSON file
curl -sX PUT http://localhost:8083/connectors/connect-file-pulse-quickstart-csv/config \
-d @./kafka-connect/connect-file-pulse-quickstart-csv.json \
--header "Content-Type: application/json" | jq;
# Copy the datasets to be published to kafka
docker cp connect-data/quickstart-musics-dataset.csv connect://tmp/kafka-connect/examples/quickstart-musics-dataset.csv;

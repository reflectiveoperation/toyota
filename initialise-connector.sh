#!/bin/sh
# Creates directory which the connector will scan for new files
docker exec -it connect mkdir -p /tmp/kafka-connect/datasets;
# Put the connector using the configuration in the JSON file
curl -sX PUT http://localhost:8083/connectors/imdb-tsv/config \
-d @./kafka-connect/connect-file-pulse-imdb-tsv.json \
--header "Content-Type: application/json" | jq;
# Copy the datasets to be published to kafka
docker cp connect-data/title.ratings.tsv connect://tmp/kafka-connect/datasets/title.ratings.tsv;

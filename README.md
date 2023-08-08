Make sure `etc/hosts` contains line `127.0.0.1 localhost broker`
`docker-compose up`
`./initialise-connector.sh`

consume messages from `ratings` topic `docker exec -it connect kafka-console-consumer --topic average-ratings --from-beginning --bootstrap-server broker:29092 --property "print.key=true" --property "key.deserializer=org.apache.kafka.common.serialization.StringDeserializer" --property "value.deserializer=org.apache.kafka.common.serialization.DoubleDeserializer"`
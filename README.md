## Prerequisites
- Docker
- Java 17+
- curl

## Architecture Diagram

![Alt text](toyota-test-architecture.jpg?raw=true "Architecture Diagram")

## About the solution

This project contains a containerised Kafka broker and a Spring Boot app that produces and consumes messages from the 
broker. In turn, the broker consumes messages from Kafka Connect which is configured to read data from a CSV file by
using the FilePulse connector. The Kafka Streams application uses a Topology which is configured to established movie
average number of votes by calculating the votes for each movie. The average number of votes is then published to the `average-votes`
topic from the Streams state store.

## How to run
- Clone the repo
- Make sure `etc/hosts` contains line `127.0.0.1 localhost broker`
- Run `docker-compose up`
- Run `./initialise-connector.sh`
- Start Spring Boot app
- Consume messages from the `average-votes` topic by executing the following command
```
docker exec -it connect kafka-console-consumer --topic average-votes --from-beginning --bootstrap-server broker:29092 
--property "print.key=true" --property "key.deserializer=org.apache.kafka.common.serialization.StringDeserializer" 
--property "value.deserializer=org.apache.kafka.common.serialization.DoubleDeserializer"
```



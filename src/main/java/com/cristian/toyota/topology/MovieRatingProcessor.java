package com.cristian.toyota.topology;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MovieRatingProcessor {

    @Value(value = "${kafka-connect.source-topic}")
    private String sourceTopic;
    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
                .stream(sourceTopic, Consumed.with(STRING_SERDE, STRING_SERDE));

        messageStream.peek((k, v) -> System.out.println(v))
                .peek((k, v) -> System.out.println("KEY: " + k));
    }
}

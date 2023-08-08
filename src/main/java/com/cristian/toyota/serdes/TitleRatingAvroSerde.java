package com.cristian.toyota.serdes;

import com.cristian.toyota.model.TitleRatingAvro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;


public class TitleRatingAvroSerde implements Serde<TitleRatingAvro> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<TitleRatingAvro> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Deserializer<TitleRatingAvro> deserializer() {
        return (topic, data) -> {
            try {
                return objectMapper.readValue(data, TitleRatingAvro.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}


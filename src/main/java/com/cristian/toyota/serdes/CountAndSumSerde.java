package com.cristian.toyota.serdes;

import com.cristian.toyota.service.CountAndSum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;

public class CountAndSumSerde implements Serde<CountAndSum> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<CountAndSum> serializer() {
        return new Serializer<CountAndSum>() {
            @Override
            public byte[] serialize(String topic, CountAndSum data) {
                try {
                    return objectMapper.writeValueAsBytes(data);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to serialize CountAndSum", e);
                }
            }
        };
    }

    @Override
    public Deserializer<CountAndSum> deserializer() {
        return new Deserializer<CountAndSum>() {
            @Override
            public CountAndSum deserialize(String topic, byte[] bytes) {
                try {
                    return objectMapper.readValue(bytes, CountAndSum.class);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to deserialize CountAndSum", e);
                }
            }
        };
    }

    @Override
    public void close() {
        // NOOP
    }

}

package com.cristian.toyota.topology;

import com.cristian.toyota.model.TitleRatingAvro;
import com.cristian.toyota.serdes.TitleRatingAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MovieRatingProcessorTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, TitleRatingAvro> inputTopic;
    private TestOutputTopic<String, Double> outputTopic;

    @BeforeEach
    public void setup() {
        StreamsBuilder builder = new StreamsBuilder();
        new MovieRatingProcessor("input-topic", builder);
        Serde<String> STRING_SERDE = Serdes.String();
        Serde<TitleRatingAvro> TITLE_RATING_AVRO_SERDE = new TitleRatingAvroSerde();

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        testDriver = new TopologyTestDriver(builder.build(), properties);

        inputTopic = testDriver.createInputTopic("input-topic", STRING_SERDE.serializer(), TITLE_RATING_AVRO_SERDE.serializer());
        outputTopic = testDriver.createOutputTopic("average-votes", STRING_SERDE.deserializer(), Serdes.Double().deserializer());
    }

    @Test
    @DisplayName("Should calculate average number of votes for a single input")
    public void correctSingleInputAbove500CalculatesCorrectAverageNumberOfVotes() {
        List<String> headers = new ArrayList<>();
        String message = new String("tt0037688\t6.7\t512");
        TitleRatingAvro titleRatingAvro = new TitleRatingAvro();
        titleRatingAvro.setPayload(new TitleRatingAvro.Payload(headers, message));

        inputTopic.pipeInput("some-key", titleRatingAvro);

        KeyValue<String, Double> result = outputTopic.readKeyValue();
        assertThat(result.key).isEqualTo("tt0037688");
        assertThat(result.value).isEqualTo(512);
    }

    @Test
    @DisplayName("Should filter out inputs with less than 500 votes")
    public void correctMultipleInputsBelow500ShouldBeFilteredOut() {
        List<String> headers = new ArrayList<>();
        String message = "tt0037688\t6.7\t100";
        TitleRatingAvro titleRatingAvro = new TitleRatingAvro();
        titleRatingAvro.setPayload(new TitleRatingAvro.Payload(headers, message));

        List<String> headers2 = new ArrayList<>();
        String message2 = "tt0037688\t6.7\t200";
        TitleRatingAvro titleRatingAvro2 = new TitleRatingAvro();
        titleRatingAvro2.setPayload(new TitleRatingAvro.Payload(headers2, message2));

        inputTopic.pipeInput("some-key", titleRatingAvro);
        inputTopic.pipeInput("some-other-key", titleRatingAvro2);

        assertThat(outputTopic.isEmpty()).isTrue();

    }

    @Test
    @DisplayName("Should calculate average number of votes for multiple inputs")
    public void correctMultipleInputsAbove500CalculatesCorrectAverageNumberOfVotes() {
        List<String> headers = new ArrayList<>();
        String message = "tt0037688\t6.7\t2000";
        TitleRatingAvro titleRatingAvro = new TitleRatingAvro();
        titleRatingAvro.setPayload(new TitleRatingAvro.Payload(headers, message));

        List<String> headers2 = new ArrayList<>();
        String message2 = "tt0037688\t6.7\t2000";
        TitleRatingAvro titleRatingAvro2 = new TitleRatingAvro();
        titleRatingAvro2.setPayload(new TitleRatingAvro.Payload(headers2, message2));

        inputTopic.pipeInput("some-key", titleRatingAvro);
        inputTopic.pipeInput("some-key", titleRatingAvro2);

        KeyValue<String, Double> result = outputTopic.readKeyValue();

        assertThat(result.key).isEqualTo("tt0037688");
        assertThat(result.value).isEqualTo(2000);
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }
}
package com.cristian.toyota.serdes;

import com.cristian.toyota.model.TitleRatingAvro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TitleRatingAvroSerdeTest {

    private TitleRatingAvroSerde underTest;

    @BeforeEach
    void setUp() {
        underTest = new TitleRatingAvroSerde();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Correct JSON Serializes to TitleRatingAvro Successfully")
    void serializerSuccessfullyCompletesWithCorrectData() throws IOException {
        Path path = Paths.get("src/test/resources/title-rating-avro-event.json");
        byte[] correctJson = Files.readAllBytes(path);
        String expectedMessage = "tt0037688\t6.7\t482";
        TitleRatingAvro actual = underTest.deserializer().deserialize("some-topic", correctJson);

        assertThat(actual.getPayload().getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Incorrect JSON Throws Exception")
    void serializerThrowsExceptionWithIncorrectData() throws IOException {
        Path path = Paths.get("src/test/resources/title-rating-avro-event-incorrect.json");
        byte[] incorrectJson = Files.readAllBytes(path);

        assertThatThrownBy(() -> underTest.deserializer().deserialize("some-topic", incorrectJson))
                .isInstanceOf(RuntimeException.class);
    }
}
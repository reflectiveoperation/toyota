package com.cristian.toyota.topology;

import com.cristian.toyota.model.TitleRating;
import com.cristian.toyota.model.TitleRatingAvro;
import com.cristian.toyota.serdes.TitleRatingSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;

@Component
public class MovieRatingProcessor {

    private final static Serde<String> STRING_SERDE = Serdes.String();
    private final static Serde<TitleRatingAvro> TITLE_RATING_SERDE = new TitleRatingSerde();

    public MovieRatingProcessor(String sourceTopic, StreamsBuilder streamsBuilder) {
        buildTopology(sourceTopic, streamsBuilder);
    }

    public void buildTopology(String sourceTopic, StreamsBuilder streamsBuilder) {
        KStream<String, TitleRatingAvro> messageStream = streamsBuilder
                .stream(sourceTopic, Consumed.with(STRING_SERDE, TITLE_RATING_SERDE));

        KStream<String, TitleRating> newStream = messageStream
                .mapValues(v -> new TitleRating(v.getPayload().getMessage()))
                .peek((k, v) -> System.out.println(v.getTconst()));
    }
}

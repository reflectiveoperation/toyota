package com.cristian.toyota.topology;

import com.cristian.toyota.model.TitleRating;
import com.cristian.toyota.model.TitleRatingAvro;
import com.cristian.toyota.serdes.CountAndSumSerde;
import com.cristian.toyota.serdes.TitleRatingAvroSerde;
import com.cristian.toyota.service.CountAndSum;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.stereotype.Component;


@Component
public class MovieRatingProcessor {

    private final static Serde<String> STRING_SERDE = Serdes.String();
    private final static Serde<TitleRatingAvro> TITLE_RATING_SERDE = new TitleRatingAvroSerde();
    private final static CountAndSumSerde COUNT_AND_SUM_SERDE = new CountAndSumSerde();

    public MovieRatingProcessor(String sourceTopic, StreamsBuilder streamsBuilder) {
        buildTopology(sourceTopic, streamsBuilder);
    }

    /**
     * This topology initially maps the incoming stream of TitleRatingAvro to a stream of TitleRating.
     * Then it filters out all the movies with less than 500 votes.
     * After that it groups the stream by tconst (Key) and aggregates the number of votes.
     * Finally, it aggregates the number of votes by tconst and calculates the average rating.
     *
     * Given more time, I would have implemented a transformer to calculate
     * (numVotes/averageNumberOfVotes) * averageRating.
     *
     * @param sourceTopic - The topic to read from.
     * @param streamsBuilder - The streams builder provided by the Spring Context.
     */
    public void buildTopology(String sourceTopic, StreamsBuilder streamsBuilder) {
        final KStream<String, TitleRating> titleRatingStream = streamsBuilder
                .stream(sourceTopic, Consumed.with(STRING_SERDE, TITLE_RATING_SERDE))
                .mapValues(value -> new TitleRating(value.getPayload().getMessage()))
                .filter((key, value) -> value.getNumVotes() > 500);

        final KGroupedStream<String, Integer> numberOfVotesById = titleRatingStream
                .map((key, rating) -> new KeyValue<>(rating.getTconst(), rating.getNumVotes()))
                .groupByKey(Grouped.with(STRING_SERDE, Serdes.Integer()));

        final KTable<String, CountAndSum> ratingCountAndSum =
                numberOfVotesById.aggregate(() -> new CountAndSum(0L, 0.0),
                (key, value, aggregate) -> {
                    aggregate.setCount(aggregate.getCount() + 1);
                    aggregate.setSum(aggregate.getSum() + value);
                    return aggregate;
                }, Materialized.<String, CountAndSum, KeyValueStore<Bytes, byte[]>>as("count-and-sum")
                        .withKeySerde(STRING_SERDE)
                        .withValueSerde(COUNT_AND_SUM_SERDE));

        final KTable<String, Double> ratingAverage =
                ratingCountAndSum.mapValues(value -> value.getSum() / value.getCount(),
                        Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("average-ratings")
                                .withKeySerde(STRING_SERDE)
                                .withValueSerde(Serdes.Double()));

        ratingAverage.toStream().to("average-ratings", Produced.with(STRING_SERDE, Serdes.Double()));


        streamsBuilder.build();


    }
}

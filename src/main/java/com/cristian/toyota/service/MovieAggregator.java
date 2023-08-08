package com.cristian.toyota.service;

import com.cristian.toyota.model.TitleRating;

/**
 * TODO: Given more time, I would have implemented this class as a Kafka Streams Transformer.
 * This class is used to aggregate the ratings of a movie.
 * It is used to calculate the score of a movie according to the following formula:
 * (numVotes/averageNumberOfVotes) * averageRating.
 */
public class MovieAggregator {
    private double totalRating;
    private int totalVotes;

    public MovieAggregator() {
        this.totalRating = 0.0;
        this.totalVotes = 0;
    }

    public MovieAggregator add(TitleRating titleRating) {
        this.totalRating += titleRating.getAverageRating();
        this.totalVotes += titleRating.getNumVotes();
        return this;
    }

    public double calculateScore(Integer averageNumberOfVotes) {
        if (totalVotes == 0) {
            return 0.0;
        }
        double averageRating = totalRating / totalVotes;
        return (totalVotes / Double.valueOf(averageNumberOfVotes)) * averageRating;
    }
}

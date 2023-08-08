package com.cristian.toyota.model;

import lombok.Data;

@Data
public class TitleRating {
    private String tconst;
    private Double averageRating;
    private Integer numVotes;

    public TitleRating(String message) {
        String[] tsvLine = message.split("\t");
        this.tconst = tsvLine[0];
        this.averageRating = Double.valueOf(tsvLine[1]);
        this.numVotes = Integer.valueOf(tsvLine[2]);
    }
}

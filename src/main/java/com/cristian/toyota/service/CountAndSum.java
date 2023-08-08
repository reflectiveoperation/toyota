package com.cristian.toyota.service;

import lombok.*;

/**
 * This class is a helper class for Kafka Streams to keep track of the number of votes and the sum of the ratings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountAndSum {
    private Long count;
    private Double sum;
}

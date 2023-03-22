package com.learneasy.user.config;


import com.avro.le.Bid;
import com.avro.le.Schedule;
import com.avro.le.ScheduleComparator;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Component
public class BidsStreamsProcessor {

    @Bean
    public KStream<String, Bid> leBidStream(StreamsBuilder streamsBuilder) {
        System.out.println("*****Inside leBidStream");
        MyAvroSerde<Bid> myRecordSerde = new MyAvroSerde<>(Bid.class);

//        KStream<String, Bid> stream = streamsBuilder.stream(
//                "le-bid",
//                Consumed.with(Serdes.String(), AvroSerdeUtils.createAvroSerde(Bid.class))
//        );
        KStream<String, Bid> stream = streamsBuilder.stream("le-bid", Consumed.with(Serdes.String(), myRecordSerde));

        return stream;
    }

    @Bean
    public KStream<String, Bid> approvedBidStream(KStream<String, Bid> leBidStream) {
        Serde<Bid> bidSerde = AvroSerdeUtils.createAvroSerde(Bid.class);
//        MyAvroSerde<Bid> bidSerde = new MyAvroSerde<>(Bid.class);
//        leBidStream.foreach((key, value) -> {
//            System.out.println("Key: " + key + " Value: " + value);
//        });

        KTable<String, Bid> reducedTable = leBidStream
                .groupByKey(Grouped.with(Serdes.String(), bidSerde))
                .reduce((aggValue, newValue) -> {
                    if (hasDuplicate(aggValue, newValue)) {
                        System.out.println("HEEEEEEEEEEE 1");
                        // If the two bids are duplicates, return the one with the earliest start time
                        ScheduleComparator scheduleComparator = new ScheduleComparator();
                        return scheduleComparator.compareByEndDateTime(
                                aggValue.getSchedules().size() > 0 ? aggValue.getSchedules().get(0) : null,
                                newValue.getSchedules().size() > 0 ? newValue.getSchedules().get(0) : null
                        ) > 0 ? aggValue : newValue;
                    } else {
                        // If the two bids are not duplicates, return the one with the latest end time
                        System.out.println("HEEEEEEEEEEE 2");
                        System.out.println("HEEEEEEEEEEE 4 "+aggValue);
                        System.out.println("HEEEEEEEEEEE 5"+newValue);
                        ScheduleComparator scheduleComparator = new ScheduleComparator();
                        return scheduleComparator.compareByEndDateTime(
                                aggValue.getSchedules().size() > 0 ? aggValue.getSchedules().get(0) : null,
                                newValue.getSchedules().size() > 0 ? newValue.getSchedules().get(0) : null
                        ) > 0 ? aggValue : newValue;                    }
                });

        KStream<String, Bid> transformedStream = reducedTable.toStream()
                .mapValues(bid -> {
                    System.out.println("Approved Bid: " + bid);
                    return bid;
                });

        transformedStream.to("le-bid-approved", Produced.with(Serdes.String(), bidSerde));


        return transformedStream;
    }


    private boolean hasDuplicate(Bid bid1, Bid bid2) {
        System.out.println("hasDuplicate called !!!!!");
        // Check if the two bids are duplicates based on their subjectId and tutorId fields
        // and whether they have overlapping schedules
        return Objects.equals(bid1.getSubjectId(), bid2.getSubjectId()) &&
                Objects.equals(bid1.getTutorId(), bid2.getTutorId()) &&
                hasOverlap(bid1.getSchedules(), bid2.getSchedules());
    }

    private boolean hasOverlap(List<Schedule> schedules1, List<Schedule> schedules2) {
        // Check if there's any overlap between the two schedules lists
        // based on their start and end times
        for (Schedule schedule1 : schedules1) {
            for (Schedule schedule2 : schedules2) {
                if (hasOverlap(schedule1.getStartDateTime(), schedule1.getEndDateTime(),
                        schedule2.getStartDateTime(), schedule2.getEndDateTime())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean hasOverlap(CharSequence startDateTime1, CharSequence endDateTime1, CharSequence startDateTime2, CharSequence endDateTime2) {
        System.out.println("in hasOverlap 2");
        LocalDateTime startTime1 = LocalDateTime.parse(startDateTime1, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LocalDateTime endTime1 = LocalDateTime.parse(endDateTime1, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LocalDateTime startTime2 = LocalDateTime.parse(startDateTime2, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LocalDateTime endTime2 = LocalDateTime.parse(endDateTime2, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println("in hasOverlap 2" +startTime1);
        System.out.println("in hasOverlap 2"+endTime1);
        System.out.println("in hasOverlap 2"+startTime2);
        System.out.println("in hasOverlap 2"+endTime2);

        // Check if the two schedules overlap
        return (startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1));
    }
}

//    private boolean hasOverlap(String startDateTime1, String endDateTime1, String startDateTime2,


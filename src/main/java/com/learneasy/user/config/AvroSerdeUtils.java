package com.learneasy.user.config;


import com.avro.le.Bid;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class AvroSerdeUtils {

    public static Serde<Bid> createAvroSerde(Class<Bid> clazz) {
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put("schema.registry.url", "http://localhost:8081");

        final Serializer<Bid> bidSerializer = new SpecificAvroSerializer<>();
        bidSerializer.configure(serdeProps, false);

        final Deserializer<Bid> bidDeserializer = new SpecificAvroDeserializer<>();
        bidDeserializer.configure(serdeProps, false);

        System.out.println("Serdes.serdeFrom(bidSerializer, bidDeserializer)");
        System.out.println(Serdes.serdeFrom(bidSerializer, bidDeserializer));
        return Serdes.serdeFrom(bidSerializer, bidDeserializer);

    }

}


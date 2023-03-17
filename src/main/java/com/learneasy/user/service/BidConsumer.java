package com.learneasy.user.service;

import com.avro.le.Bid;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class BidConsumer {

    @KafkaListener(topics = "le-bid", groupId = "bid-group-id9")
    public void receiveBid(ConsumerRecord<Long, com.avro.le.Bid> bid) {
        com.avro.le.Bid bidData = bid.value();
        System.out.println("Received bid: " + bidData.getSchedules().get(0).getEndDateTime());
    }
    public static class BidDeserializer implements Deserializer<Bid> {

        public BidDeserializer(){

        }
        @Override
        public com.avro.le.Bid deserialize(String topic, byte[] data) {
            DatumReader<Bid> reader = new SpecificDatumReader<>(com.avro.le.Bid.class);
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
            try {
                return reader.read(null, decoder);
            } catch (IOException e) {
                throw new SerializationException("Error deserializing Avro message", e);
            }
        }

        @Override
        public void close() {

        }
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Bid> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, Bid> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    private ConsumerFactory<Long, Bid> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bid-group-id9");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // Configure the value deserializer using ErrorHandlingDeserializer
        Deserializer<Bid> valueDeserializer = new BidDeserializer();
        valueDeserializer.configure(Collections.emptyMap(), false);
        ErrorHandlingDeserializer<Bid> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(valueDeserializer);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, errorHandlingDeserializer.getClass());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, BidConsumer.BidDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<>(props);
    }

}

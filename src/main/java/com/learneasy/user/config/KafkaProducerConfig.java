package com.learneasy.user.config;

//import com.fasterxml.jackson.databind.JsonSerializer;
import com.avro.le.Bid;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@ComponentScan(basePackages = {"com.learneasy.user"})
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<Long, Bid> producerFactory() {
        System.out.println("---------------loading producerFactory-------------------START");
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, "AvroProducer");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaProducerConfig.BidSerializer.class.getName());
        configProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        System.out.println("---------------loading producerFactory-------------------"+bootstrapServers);
        System.out.println("---------------loading producerFactory-------------------END");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, Bid> kafkaTemplate() {
        return new KafkaTemplate<Long, Bid>(producerFactory());
    }


    public static class BidSerializer implements Serializer<Bid> {
        public BidSerializer() {
            // no-arg constructor
        }
        @Override
        public byte[] serialize(String topic, com.avro.le.Bid data) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                DatumWriter<Bid> writer = new SpecificDatumWriter<>(com.avro.le.Bid.class);
                writer.write(data, encoder);
                encoder.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out.toByteArray();
        }

        @Override
        public void close() {

        }
    }

}


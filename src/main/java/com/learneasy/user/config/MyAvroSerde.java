package com.learneasy.user.config;

import com.avro.le.Bid;
import com.learneasy.user.service.BidConsumer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MyAvroSerde<T extends SpecificRecord> implements Serde<T> {

    private final Serde<T> inner;

    public MyAvroSerde(Class<T> clazz) {
        KafkaAvroSerializer serializer = new KafkaAvroSerializer();
        serializer.configure(Collections.singletonMap("schema.registry.url", "http://localhost:8081"), false);

        BidConsumer.BidDeserializer deserializer1 = new BidConsumer.BidDeserializer();
        Map<String, Object> props = new HashMap<>();
       //props.put(Collections.singletonMap("schema.registry.url", "http://localhost:8081"), false);

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bid-group-id9");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        // Configure the value deserializer using ErrorHandlingDeserializer
        Deserializer<Bid> valueDeserializer = new BidConsumer.BidDeserializer();
        valueDeserializer.configure(Collections.emptyMap(), false);
        ErrorHandlingDeserializer<Bid> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(valueDeserializer);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, errorHandlingDeserializer.getClass());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, BidConsumer.BidDeserializer.class.getName());


        Serializer<T> avroSerializer = (topic, data) -> serializer.serialize(topic, data);
        Deserializer<T> avroDeserializer = (topic, data) -> (T) deserializer1.deserialize(topic, data);

        this.inner = Serdes.serdeFrom(avroSerializer, avroDeserializer);
    }

    @Override
    public Serializer<T> serializer() {
        return inner.serializer();
    }

    @Override
    public Deserializer<T> deserializer() {
        return inner.deserializer();

    }

    private T deserializeMe(String topic, byte[] data, Class<T> clazz) {

        DatumReader<T> reader = new SpecificDatumReader<>(clazz);
        Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        try {
            Object s =  reader.read(null, decoder);
            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            System.out.println(s);
            return reader.read(null, decoder);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing Avro object", e);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        inner.configure(configs, isKey);
    }

    @Override
    public void close() {
        inner.close();
    }
}

package com.learneasy.user.service;

import com.avro.le.Bid;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericDatumWriter;
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

//        public BidDeserializer(){
//
//        }
        private final KafkaAvroDeserializer avroDeserializer;

        public BidDeserializer() {
            Map<String, Object> configs = new HashMap<>();
            configs.put("schema.registry.url", "http://localhost:8081");
            configs.put("specific.avro.reader", true);
            avroDeserializer = new KafkaAvroDeserializer(new CachedSchemaRegistryClient("http://localhost:8081", 100), configs);
        }
        //        @Override
//        public com.avro.le.Bid deserialize(String topic, byte[] data) {
//            DatumReader<Bid> reader = new SpecificDatumReader<>(com.avro.le.Bid.class);
//            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
//            try {
//                return reader.read(null, decoder);
//            } catch (IOException e) {
//                throw new SerializationException("Error deserializing Avro message", e);
//            }
//        }

        @Override
        public Bid deserialize(String topic, byte[] data) {
            Object genericRecord = avroDeserializer.deserialize(topic, data);
//            if(true) return (Bid)genericRecord;
            DatumReader<Bid> reader = new SpecificDatumReader<>(Bid.class);
            Decoder decoder = null;
            try {
                //in above example data directly comes as byte array
                // and no connection to avro registry to deserialize
                //here it forces to connect to avro registry
                decoder = DecoderFactory.get().binaryDecoder(genericRecordToByteArray(genericRecord), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (RestClientException e) {
                throw new RuntimeException(e);
            }
            try {
                return reader.read(null, decoder);
            } catch (IOException e) {
                throw new SerializationException("Error deserializing Avro message", e);
            }
        }

        private byte[] genericRecordToByteArray(Object genericRecord) throws IOException, RestClientException {
            String schemaName = ((GenericContainer) genericRecord).getSchema().getFullName();
            String schemaRegistryUrl = "http://localhost:8081"; // replace with the URL of your schema registry
            SchemaRegistryClient schemaRegistry = new CachedSchemaRegistryClient(schemaRegistryUrl, 100);
            int schemaId = schemaRegistry.getLatestSchemaMetadata("le-bid-value").getId();
            Schema schema = schemaRegistry.getByID(schemaId);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(baos, null);
            DatumWriter<Object> writer = new GenericDatumWriter<>(schema);
            writer.write(genericRecord, encoder);
            encoder.flush();
            return baos.toByteArray();        }
        @Override
        public void close() {
            avroDeserializer.close();
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
       // props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

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

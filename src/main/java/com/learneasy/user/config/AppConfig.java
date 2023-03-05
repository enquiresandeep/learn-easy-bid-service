package com.learneasy.user.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.learneasy.user.infrastructure.db.DateToZonedDateTimeConverter;
import com.learneasy.user.infrastructure.db.ZonedDateTimeConverter;
import com.learneasy.user.infrastructure.mapper.BidMapper;
import com.learneasy.user.infrastructure.mapper.SubjectMapper;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = {"com.learneasy.user"})
public class AppConfig {
    @Bean
    public SubjectMapper subjectMapper() {
        return Mappers.getMapper(SubjectMapper.class);
    }
    @Bean
    public BidMapper bidMapper() {
        return Mappers.getMapper(BidMapper.class);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions1() {
        return new MongoCustomConversions(Arrays.asList(new ZonedDateTimeConverter(),new DateToZonedDateTimeConverter()));
    }



    @Value("${spring.kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        return new CachedSchemaRegistryClient(schemaRegistryUrl, 10);
    }
}

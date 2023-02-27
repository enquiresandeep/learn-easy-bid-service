package com.learneasy.user.config;

import com.learneasy.user.infrastructure.DateToZonedDateTimeConverter;
import com.learneasy.user.infrastructure.ZonedDateTimeConverter;
import com.learneasy.user.infrastructure.mapper.AddressMapper;
import com.learneasy.user.infrastructure.mapper.PhoneMapper;
import com.learneasy.user.infrastructure.mapper.BidMapper;
import com.learneasy.user.infrastructure.mapper.SubjectMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

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
    public PhoneMapper phoneMapper() {
        return Mappers.getMapper(PhoneMapper.class);
    }
    @Bean
    public AddressMapper addressMapper() {
        return Mappers.getMapper(AddressMapper.class);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions1() {
        return new MongoCustomConversions(Arrays.asList(new ZonedDateTimeConverter(),new DateToZonedDateTimeConverter()));
    }


}

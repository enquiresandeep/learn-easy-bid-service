package com.learneasy.user.config;

import com.learneasy.user.infrastructure.mapper.AddressMapper;
import com.learneasy.user.infrastructure.mapper.PhoneMapper;
import com.learneasy.user.infrastructure.mapper.StudentMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.learneasy.user"})
public class AppConfig {
    @Bean
    public StudentMapper studentMapper() {
        return Mappers.getMapper(StudentMapper.class);
    }
    @Bean
    public PhoneMapper phoneMapper() {
        return Mappers.getMapper(PhoneMapper.class);
    }
    @Bean
    public AddressMapper addressMapper() {
        return Mappers.getMapper(AddressMapper.class);
    }
}

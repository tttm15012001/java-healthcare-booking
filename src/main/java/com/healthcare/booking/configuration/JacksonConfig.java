package com.healthcare.booking.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        JavaTimeModule module = new JavaTimeModule();
//        module.addSerializer(
//                LocalDateTime.class,
//                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
//        );
//        objectMapper.registerModule(module);
//        return objectMapper;
//    }
}

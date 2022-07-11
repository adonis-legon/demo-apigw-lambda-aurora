package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonConverter {
    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }
}

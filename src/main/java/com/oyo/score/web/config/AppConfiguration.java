package com.oyo.score.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oyo.score.web.utils.MapUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AppConfiguration {
//    @Bean
//    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//        return new Jackson2ObjectMapperBuilder();
//    }

    private final ObjectMapper objectMapper;

    public AppConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void init() {
        MapUtil.initialize(objectMapper);
    }
}

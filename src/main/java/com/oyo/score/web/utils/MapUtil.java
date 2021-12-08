package com.oyo.score.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapUtil {

    private static final Logger log = LoggerFactory.getLogger(MapUtil.class);

    private static ObjectMapper objectMapper;

    public static void initialize(ObjectMapper objectMapper) {
        MapUtil.objectMapper = objectMapper;
    }

    public static String parseObjectToJsonString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Error occurred while parsing object to json.", e);
            return "";
        }
    }
}

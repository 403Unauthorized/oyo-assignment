package com.oyo.score.web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class TestUtil {

    private static final Logger logger = LoggerFactory.getLogger(TestUtil.class);

    public static File getFile(String fileName) {
        File inputFile = null;
        try {
            inputFile = new File(TestUtil.class.getResource("/" + fileName).toURI());
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException while getting file: '" + fileName + "', Returning null...", e);
        }
        return inputFile;
    }

    public static <T> T readValue(File file, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(file, clazz);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T convertObjectTo(Object value, Class<T> valueType) {
        return new ObjectMapper().convertValue(value, valueType);
    }

    public static URI buildUri(String uri, Map<String, Object> requestParams) {
        if (!ObjectUtils.isEmpty(requestParams) && requestParams.size() > 0) {
            uri += "?";
        } else {
            return URI.create(uri);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.lastIndexOf("&"));
        uri += sb.toString();
        return URI.create(uri);
    }
}

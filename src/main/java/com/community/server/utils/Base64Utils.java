package com.community.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.util.Pair;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Base64Utils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @SafeVarargs
    public static String base64Encoded(Pair<String, Object>... entries) {
        try {
            return Base64.getEncoder().encodeToString(
                    objectMapper
                            .writer()
                            .writeValueAsString(entries)
                            .getBytes(UTF_8)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

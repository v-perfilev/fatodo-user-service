package com.persoff68.fatodo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class TestUtils {

    public static MultiValueMap<String, String> objectToMap(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(object);
        Map<String, Object> map = objectMapper.readValue(json, Map.class);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        map.forEach((key, value) -> {
            if (value == null) {
                multiValueMap.add(key, null);
            } else if (value instanceof String) {
                multiValueMap.add(key, (String) value);
            } else {
                multiValueMap.add(key, String.valueOf(value));
            }
        });
        return multiValueMap;
    }

}

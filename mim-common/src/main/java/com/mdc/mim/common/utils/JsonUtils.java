package com.mdc.mim.common.utils;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    /**
     * 使用jackjson将对象转换为json字符串
     * 
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON string.", e);
        }
    }

    /**
     * 将对象转换为一个map
     * 
     * @param <T>
     * @param object
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Map object2Map(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(object, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to map.", e);
        }
    }
}

package com.github.artemgrishin322.restaurantvoting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@UtilityClass
public class JsonUtil {
    private static ObjectMapper mapper;

    public static void setMapper(ObjectMapper mapper) {
        JsonUtil.mapper = mapper;
    }

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        ObjectReader reader = mapper.readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", ex);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", ex);
        }
    }

    public static <T> String writeValue(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Invalid write to JSON:\n'" + obj + "'", ex);
        }
    }

    public static <T> String writeAdditionProps(T obj, String addName, Object addValue) {
        return writeAdditionProps(obj, Map.of(addName, addValue));
    }

    public static <T> String writeAdditionProps(T obj, Map<String, Object> addProps) {
        Map<String, Object> map = mapper.convertValue(obj, new TypeReference<>() {});
        map.putAll(addProps);
        return writeValue(map);
    }
}

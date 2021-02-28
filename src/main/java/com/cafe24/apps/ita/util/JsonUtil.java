package com.cafe24.apps.ita.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import java.io.IOException;

public final class JsonUtil {

    /**
     * Gson
     */
    public static Gson GSON = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    /**
     * objet to json
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJSON(T t) {
        return GSON.toJson(t);
    }

    /**
     * json to object
     *
     * @param responseString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJSON(String responseString, Class<T> clazz) {
        T t;
        if (clazz.isAssignableFrom(responseString.getClass())) {
            t = clazz.cast(responseString);
        } else {
            t = GSON.fromJson(responseString, clazz);
        }
        return t;
    }

    /**
     * convertJsonNode 공백/개행 제거
     *
     * @param value
     * @return
     */
    public static String convertJsonNode(String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readValue(value, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode.toString();
    }

}

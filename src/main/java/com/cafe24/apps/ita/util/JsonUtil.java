package com.cafe24.apps.ita.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.io.IOException;

public final class JsonUtil {

    /**
     * Gson
     */
    private static Gson GSON = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    /**
     * prettyGSON
     */
    private static Gson prettyGSON = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
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
     * pretty json string
     *
     * @param strJson
     * @return
     */
    public static String convetPrettyJson(String strJson) {
        JsonElement jsonElement = JsonParser.parseString(strJson);
        return prettyGSON.toJson(jsonElement);
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

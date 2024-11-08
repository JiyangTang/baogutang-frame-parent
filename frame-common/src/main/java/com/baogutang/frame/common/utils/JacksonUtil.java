package com.baogutang.frame.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: JacksonUtil
 * @author: nikooh
 * @date: 2024/08/06 : 10:50
 */
@Slf4j
public class JacksonUtil {

    private JacksonUtil() {
        // empty private constructor
    }

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    static {

        // yyyy-MM-dd HH:mm:ss
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        //  JavaTimeModule  Java 8
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        //
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //  ObjectMapper  null
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //  ObjectMapper  Bean ()
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //  ObjectMapper  JSON  Java
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    /**
     * json
     *
     * @param content
     * @return json
     */
    public static String toJson(Object content) {
        String result = StringUtils.EMPTY;
        if (Objects.isNull(content)) {
            return result;
        }
        try {
            result = OBJECT_MAPPER.writeValueAsString(content);
        } catch (Exception e) {
            log.error(">>>>>>>>>>parse object to json fail:{}<<<<<<<<<<", e.getMessage());
        }
        return result;
    }

    /**
     *
     *
     * @param content
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String content, Class<T> clazz) {
        T result = null;
        if (StringUtils.isBlank(content)) {
            return result;
        }
        try {
            result = OBJECT_MAPPER.readValue(content, clazz);
        } catch (Exception e) {
            log.error(">>>>>>>>>>parse json to object fail:{}<<<<<<<<<<", e.getMessage());
        }
        return result;
    }

    /**
     *
     *
     * @param content
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String content, TypeReference<T> type) {
        T result = null;
        if (StringUtils.isBlank(content)) {
            return result;
        }
        try {
            result = OBJECT_MAPPER.readValue(content, type);
        } catch (Exception e) {
            log.error(">>>>>>>>>>parse json to object fail:{}<<<<<<<<<<", e.getMessage());
        }
        return result;
    }

    /**
     * jsonMap
     *
     * @param content
     * @param keyType   key
     * @param valueType value
     * @param <K>       Class<K>
     * @param <V>       Class<V>
     * @return Map
     */
    public static <K, V> Map<K, V> jsonToMap(String content, Class<K> keyType, Class<V> valueType) {
        Map<K, V> result = new HashMap<>();
        if (StringUtils.isBlank(content) || Objects.isNull(keyType) || Objects.isNull(valueType)) {
            return result;
        }
        try {
            result = OBJECT_MAPPER.readValue(content, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error(">>>>>>>>>>parse json to map fail:{}<<<<<<<<<<", e.getMessage());
        }
        return result;
    }

    public static <K, V> Map<K, V> beanToMap(Object content, Class<K> keyType, Class<V> valueType) {
        Map<K, V> result = new HashMap<>();
        if (Objects.isNull(content) || Objects.isNull(keyType) || Objects.isNull(valueType)) {
            return result;
        }
        try {
            result = OBJECT_MAPPER.convertValue(content, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error(">>>>>>>>>>parse bean to map fail:{}<<<<<<<<<<", e.getMessage());
        }
        return result;
    }

    /**
     * jsonJsonNode
     *
     * @param content
     * @return JsonNode
     */
    public static JsonNode jsonToNode(String content) {
        JsonNode jsonNode = null;
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            jsonNode = OBJECT_MAPPER.readTree(content);
        } catch (Exception e) {
            log.error(">>>>>>>>>>parse json to node fail:{}<<<<<<<<<<", e.getMessage());
        }
        return jsonNode;
    }
}

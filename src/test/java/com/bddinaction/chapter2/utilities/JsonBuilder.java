package com.bddinaction.chapter2.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by donald on 2015/04/19.
 */
public class JsonBuilder {
    final Logger logger = LoggerFactory.getLogger(JsonBuilder.class);
    private ObjectMapper mapper;

    public JsonBuilder() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
    }

    public String build(final Object object) {
        String value = null;
        try {
            value = mapper.writeValueAsString(object);
            logger.info(value);
        } catch (JsonProcessingException e) {
            logger.error("Exception throw... {}", e);
        }
        return value;
    }

    public <T> T build(final String value, Class<T> clazz) {
        try {
            T object = mapper.readValue(value, clazz);
            logger.info(mapper.writeValueAsString(object));
            return object;
        } catch (IOException e) {
            logger.error("Exception throw... {}", e);
        }
        return null;
    }

    public <T> T build(final String value, final TypeReference reference) {
        T object = null;
        try {
            object = mapper.readValue(value, reference);
        } catch (IOException e) {
            logger.error("Exception throw... {}", e);
        }
        return object;
    }
}

package com.example.spring.web.core.json;

import java.io.IOException;

import com.example.spring.common.StringEscapeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;

/**
 * string xss过滤
 * 
 * @author huss
 */
@Slf4j
public class String2EscapeHtml4Module extends SimpleModule {

    public String2EscapeHtml4Module() {
        super(PackageVersion.VERSION);
        super.addDeserializer(String.class, new String2EscapeHtml4Deserializer());
    }

    static class String2EscapeHtml4Deserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

            String value = p.getValueAsString();
            log.debug(">>>>> String2EscapeHtml4Deserializer. name：{}, value：{}", p.currentName(), value);
            if (value != null) {
                return StringEscapeUtils.escapeHtml4(value);
            }
            return value;

        }
    }
}

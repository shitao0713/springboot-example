/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0; you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.example.spring.web.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.spring.common.StringPool;
import com.example.spring.common.StringUtils;
import com.example.spring.web.core.json.BladeBeanSerializerModifier;
import com.example.spring.web.core.json.MyJackson2ObjectMapperBuilder;
import com.example.spring.web.core.support.Exceptions;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Jackson工具类
 *
 * @author Chill
 */
@Slf4j
public class JsonUtil {

    /**
     * 将对象序列化成json字符串
     *
     * @param value
     *            javaBean
     * @param <T>
     *            T 泛型标记
     * @return jsonString json字符串
     */
    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object
     *            javaBean
     * @return jsonString json字符串
     */
    public static byte[] toJsonAsBytes(Object object) {
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param content
     *            content
     * @param valueType
     *            class
     * @param <T>
     *            T 泛型标记
     * @return Bean
     */
    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param content
     *            content
     * @param typeReference
     *            泛型类型
     * @param <T>
     *            T 泛型标记
     * @return Bean
     */
    public static <T> T parse(String content, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(content, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param bytes
     *            json bytes
     * @param valueType
     *            class
     * @param <T>
     *            T 泛型标记
     * @return Bean
     */
    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        try {
            return getInstance().readValue(bytes, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param bytes
     *            bytes
     * @param typeReference
     *            泛型类型
     * @param <T>
     *            T 泛型标记
     * @return Bean
     */
    public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(bytes, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in
     *            InputStream
     * @param valueType
     *            class
     * @param <T>
     *            T 泛型标记
     * @return Bean
     */
    public static <T> T parse(InputStream in, Class<T> valueType) {
        try {
            return getInstance().readValue(in, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in
     *            InputStream
     * @param typeReference
     *            泛型类型
     * @param <T>
     *            T 泛型标记
     * @return Bean
     */
    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(in, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成List对象
     * 
     * @param content
     *            content
     * @param valueTypeRef
     *            class
     * @param <T>
     *            T 泛型标记
     * @return List
     */
    public static <T> List<T> parseArray(String content, Class<T> valueTypeRef) {
        try {

            if (!StringUtils.startsWithIgnoreCase(content, StringPool.LEFT_SQ_BRACKET)) {
                content = StringPool.LEFT_SQ_BRACKET + content + StringPool.RIGHT_SQ_BRACKET;
            }
            List<Map<String, Object>> list =
                getInstance().readValue(content, new TypeReference<List<Map<String, Object>>>() {});
            List<T> result = new ArrayList<>();
            for (Map<String, Object> map : list) {
                result.add(toPojo(map, valueTypeRef));
            }
            return result;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw Exceptions.unchecked(e);
        }
    }

    public static Map<String, Object> toMap(String content) {
        try {
            return getInstance().readValue(content, Map.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw Exceptions.unchecked(e);
        }
    }

    public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
        try {
            Map<String, Map<String, Object>> map =
                getInstance().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {});
            Map<String, T> result = new HashMap<>(16);
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), toPojo(entry.getValue(), valueTypeRef));
            }
            return result;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw Exceptions.unchecked(e);
        }
    }

    public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonString
     *            jsonString
     * @return jsonString json字符串
     */
    public static JsonNode readTree(String jsonString) {
        try {
            return getInstance().readTree(jsonString);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param in
     *            InputStream
     * @return jsonString json字符串
     */
    public static JsonNode readTree(InputStream in) {
        try {
            return getInstance().readTree(in);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param content
     *            content
     * @return jsonString json字符串
     */
    public static JsonNode readTree(byte[] content) {
        try {
            return getInstance().readTree(content);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser
     *            JsonParser
     * @return jsonString json字符串
     */
    public static JsonNode readTree(JsonParser jsonParser) {
        try {
            return getInstance().readTree(jsonParser);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    private static class JacksonHolder {
        private static ObjectMapper INSTANCE;
        static {
            INSTANCE = MyJackson2ObjectMapperBuilder.json().build();
            // null 处理
            INSTANCE.setSerializerFactory(
                INSTANCE.getSerializerFactory().withSerializerModifier(new BladeBeanSerializerModifier()));
            INSTANCE.getSerializerProvider()
                .setNullValueSerializer(BladeBeanSerializerModifier.NullJsonSerializers.STRING_JSON_SERIALIZER);
        }
    }
}

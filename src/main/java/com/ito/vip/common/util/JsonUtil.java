package com.ito.vip.common.util;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {

    private JsonUtil(){

    }

    public static String convertObjectToJSON(Object object){
        String result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        if(object!=null){
            try {
                result = objectMapper.writeValueAsString(object);
            } catch (Exception exception) {
                throw ExceptionUtil.wrapAsRuntimeException(exception);
            }
        }
        return result;
    }

    public static <T> T convertJSONToObject(String json, Class<T> valueType){
        T result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        if (!StringUtils.isEmpty(json)) {
            try {
                result = objectMapper.readValue(json, valueType);
            } catch (Exception exception) {
                throw ExceptionUtil.wrapAsRuntimeException(exception);
            }
        }
        return result;
    }

    public static Object convertJSONToObject(String json){
        return convertJSONToObject(json, Object.class);
    }
}

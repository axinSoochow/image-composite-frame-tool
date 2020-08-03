package com.ke.store.vr.common.config.enumm;

import com.ke.store.vr.domain.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author axin
 * @summary get 请求时 枚举反序列化处理
 */
public class EnumConvertHandler implements ConverterFactory<String, BaseEnum> {

    private static final Map<Class, Converter> converterMap = new WeakHashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter result = converterMap.get(targetType);
        if(result == null) {
            result = new StringEnumConverter<>(targetType);
            converterMap.put(targetType, result);
        }
        return result;
    }

    class StringEnumConverter<T extends BaseEnum> implements Converter<String, T> {
        private Map<String, T> enumMap = new HashMap<>();

        public StringEnumConverter(Class<T> enumType) {
            T[] enums = enumType.getEnumConstants();
            for(T e : enums) {
                enumMap.put(e.getCode(), e);
            }
        }

        @Override
        public T convert(String source) {
            T result = enumMap.get(source);
            if(result == null) {
                return null;
            }
            return result;
        }
    }
}

package com.ke.store.vr.common.config.redis;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class KeyGeneratorCustomize implements KeyGenerator {

    private final static int NO_PARAM_KEY = 0;

    private String cachePrefix;

    public KeyGeneratorCustomize(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        char sp = ':';
        StringBuilder strBuilder = new StringBuilder(cachePrefix);
        strBuilder.append(sp);
        // 类名
        strBuilder.append(target.getClass().getSimpleName());
        strBuilder.append(sp);
        // 方法名
        strBuilder.append(method.getName());
        strBuilder.append(sp);
        if (params.length > 0) {
            // 参数值
            for(int i=0;i< params.length; i++){
                Object object = params[i];
                if(object == null){
                    strBuilder.append("null");
                }else{
                    if (BeanUtils.isSimpleValueType(object.getClass())) {
                        strBuilder.append(object);
                    } else {
                        strBuilder.append(JSONObject.toJSON(object).hashCode());
                    }
                }

                if (i != params.length - 1) strBuilder.append(",");
            }
        } else {
            strBuilder.append(NO_PARAM_KEY);
        }
        return strBuilder.toString();
    }
}

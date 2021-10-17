package com.axin.idea.validate.domain;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author axin
 * @summary 校验参数接口 Map形式的参数或实现此接口的对象参数 可以进行校验
 * @since 2020年02月17日
 */
public abstract class AxinValidate<T> {

    public static final String checkResultFieldCode = "checkResultFieldCode";
    public static final String checkResultFieldName = "checkResultFieldName";

    /* 校验结果 */
    protected Map<String, String> checkErrResult;

    /**
     * 参数转化为JSON
     * @return
     */
    public Map<String,Object> getParamsMap(T t){
        return JSON.parseObject(JSON.toJSONString(t), Map.class);
    }

    public Map<String, String> getCheckErrResult() {
        return checkErrResult;
    }

    public void setCheckErrResult(Map<String, String> checkErrResult) {
        this.checkErrResult = checkErrResult;
    }
}

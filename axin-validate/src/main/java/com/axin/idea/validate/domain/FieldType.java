package com.axin.idea.validate.domain;


import com.axin.idea.validate.support.ValidateSupport;

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * 校验支持类型枚举
 */
public enum FieldType {
    TimeStamp("TimeStamp", f -> ValidateSupport.isTimeStamp(f), (k, v) -> java.lang.String.format("[%s]的值[%s]不是时间", k, v)),
    String("String", f -> true, (k, v) -> ""),
    Number("Number", f -> ValidateSupport.isNumber(f),(k,v)->java.lang.String.format("[%s]的值[%s]不是数字", k, v)),
    Boolean("Boolean", f -> ValidateSupport.isBoolean(f), (k, v) -> java.lang.String.format("[%s]的值[%s]不是Boolean类型", k, v)),
    //数组类型变量中的元素类型
    Object("Object", null, null),
    //数组类型
    List("List", null, null),
    ;

    private String type;
    private Predicate<String> checkEngine;
    private BiFunction<String, String, String> errMsg;

    FieldType(String type, Predicate<String> checkEngine
            , BiFunction<String, String, String> errMsg) {
        this.type = type;
        this.checkEngine = checkEngine;
        this.errMsg = errMsg;
    }

    public static FieldType getFieldType(String type) {
        FieldType fieldType = ValidateSupport.getEnumMap(FieldType.class).get(type);
        if (fieldType == null) {
            return null;
        }
        return fieldType;
    }

    public String getType() {
        return type;
    }

    public Predicate<String> getCheckEngine() {
        return checkEngine;
    }

    public BiFunction<String, String, String> getErrMsg() {
        return errMsg;
    }
}

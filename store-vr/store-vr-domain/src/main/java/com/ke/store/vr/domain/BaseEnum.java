package com.ke.store.vr.domain;


/**
 * @JsonValue： 在序列化时，只序列化 @JsonValue 注解标注的值
 */
public interface BaseEnum {
    String getCode();
    String getDesc();
}
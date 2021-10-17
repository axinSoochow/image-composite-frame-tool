package com.axin.idea.validate.domain;

import lombok.Data;

import java.util.List;

/**
 * @summary 校验单元
 */
@Data
public class FieldCheckUnit {

    private String fieldCode;
    private String fieldName;
    /**
     * 变量类型
     */
    private String fieldType;

    private Integer maxLen;
    private Integer minLen;
    private Double maxSize;
    private Double minSize;
    /**
     * 不等于
     */
    private Double notEqual;

    /**
     * 小数位
     */
    private Integer decimalPlace;
    /**
     * 是否必填
     */
    private Boolean require;

    /**
     * 扩展校验
     */
    private String ext;
    /**
     * 枚举code校验
     */
    private List<String> enumList;

    /**
     * 正则表达式
     */
    private String regular;
}

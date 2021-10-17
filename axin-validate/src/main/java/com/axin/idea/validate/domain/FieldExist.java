package com.axin.idea.validate.domain;

import java.util.function.BiFunction;

/**
 * @author axin
 * @summary 依赖校验枚举配置类
 * @since 2021-10-17
 */
public enum FieldExist {
    城市("City", "在XX系统中校验城市code是否合法", (k, v) -> String.format("[%s]的值[%s]在依赖系统中不存在", k, v)),

    ;

    private String code;
    private String desc;
    private BiFunction<String, String, String> errMsg;

    FieldExist(String code, String desc, BiFunction<String, String, String> errMsg) {
        this.code = code;
        this.desc = desc;
        this.errMsg = errMsg;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public BiFunction<String, String, String> getErrMsg() {
        return errMsg;
    }
}

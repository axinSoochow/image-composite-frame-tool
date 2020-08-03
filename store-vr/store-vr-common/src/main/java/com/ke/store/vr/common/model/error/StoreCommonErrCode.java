package com.ke.store.vr.common.model.error;

import com.ke.store.vr.domain.LogLevel;
import com.ke.store.vr.domain.StoreError;
import lombok.Getter;

/**
 * @author axin
 * @summary Smp公共模块异常码
 * @since 2020-07-03
 */
public enum StoreCommonErrCode implements StoreError {
    无权访问(100001, "无权访问", LogLevel.WARN),
    请求参数错误(100002, "请求参数错误"),
    业务验证异常(100003, "业务验证异常"),
    请求资源不存在(100004, "请求资源不存在", LogLevel.WARN),

    依赖服务异常(100010, "依赖服务异常"),
    ;

    @Getter
    private int code;

    @Getter
    private String message;

    private LogLevel level;

    StoreCommonErrCode(int code, String message) {
        this.code = code;
        this.message = message;
        this.level = LogLevel.ERROR;
    }

    StoreCommonErrCode(int code, String message, LogLevel level) {
        this.code = code;
        this.message = message;
        this.level = level;
    }

    public LogLevel getLevel() {
        return this.level;
    }

}

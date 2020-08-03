package com.ke.store.vr.domain;

import lombok.Data;

/**
 * 默认的store异常信息上下文实现
 */
@Data
public class DefaultStoreErrorContext implements StoreError {

    private int code;

    private String message;

    private Object detail;

    private LogLevel logLevel;

    public DefaultStoreErrorContext(StoreError error) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.logLevel = error.getLevel();
    }

    public DefaultStoreErrorContext(int code, String message) {
        this.code = code;
        this.message = message;
        this.logLevel = LogLevel.ERROR;
    }

    public DefaultStoreErrorContext(StoreError error, LogLevel logLevel) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.logLevel = logLevel;
    }

    public DefaultStoreErrorContext(StoreError error, Object detail) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.logLevel = error.getLevel();
        this.detail = detail;
    }

    public DefaultStoreErrorContext(StoreError error, String msg) {
        this.code = error.getCode();
        this.message = msg;
        this.logLevel = error.getLevel();
        this.detail = error.getDetail();
    }

    @Override
    public LogLevel getLevel(){
        return this.logLevel;
    }

    @Override
    public Object getDetail(){return this.detail;}

}

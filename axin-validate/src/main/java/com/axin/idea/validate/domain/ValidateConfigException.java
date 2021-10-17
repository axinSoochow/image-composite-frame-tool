package com.axin.idea.validate.domain;

/**
 * 校验组件配置文件错误
 */
public class ValidateConfigException extends RuntimeException {

    public ValidateConfigException() {
    }

    public ValidateConfigException(String message) {
        super(message);
    }

    public ValidateConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateConfigException(Throwable cause) {
        super(cause);
    }

    public ValidateConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.axin.idea.validate.domain;

/**
 * @author axin
 * @since 2020-02-21
 * @summary 跳出校验
 */
public class ValidateSkipException extends RuntimeException {

    public ValidateSkipException() {
        super();
    }

    public ValidateSkipException(String message) {
        super(message);
    }

    public ValidateSkipException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateSkipException(Throwable cause) {
        super(cause);
    }

    protected ValidateSkipException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

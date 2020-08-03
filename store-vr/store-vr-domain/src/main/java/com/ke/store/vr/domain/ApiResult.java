package com.ke.store.vr.domain;
import lombok.Data;

/**
 * @author axin
 * @summary 系统交互返回报文结构
 */
@Data
public class ApiResult<T> {

    /**
     * 系统返回码
     */
    private int code;

    /**
     * 系统返回消息
     */
    private String message;

    /**
     * 详情
     */
    private T detail;

    public ApiResult() {
    }

    public ApiResult(StoreError error, T detail) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.detail = detail;
    }

    public ApiResult(int code, String message, T detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public static<T> ApiResult<T> OK() {
        return new ApiResult<>(StoreError.OK, null);
    }

    public static<T> ApiResult<T> OK(T detail) {
        return new ApiResult<>(StoreError.OK, detail);
    }

    public static ApiResult response(ApiResult old) {
        return new ApiResult(old.getCode(), old.getMessage(), null);
    }

    public static ApiResult response(StoreError errCode) {
        return new ApiResult(errCode, errCode.getDetail());
    }

    public static <T> ApiResult response(StoreError errCode, T detail) {
        return new ApiResult(errCode, detail);
    }
}

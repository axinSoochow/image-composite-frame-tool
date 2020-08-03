package com.ke.store.vr.common.model.error;

import com.ke.store.vr.domain.StoreError;
import lombok.Getter;

/**
 * @author axin
 * @since 2020-07-02
 * @summary 商城平台api 统一异常处理
 */
public class StoreApiException extends RuntimeException {

    @Getter
    private StoreError errCode;

    public StoreApiException(StoreError errCode) {
        this.errCode = errCode;
    }

    @Override
    public String getMessage() {
        return errCode.getMessage();
    }

}

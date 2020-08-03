package com.ke.store.vr.common.model.error;

import com.ke.store.vr.domain.StoreError;
import lombok.Getter;
import lombok.Setter;

/**
 * @author axin
 * @summary 电商中台内部异常 —— 需要自抛自接
 */
public class StoreInnerException extends RuntimeException {

    @Getter
    @Setter
    private StoreError errCode;

    public StoreInnerException(StoreError errCode) {
        this.errCode = errCode;
    }

    @Override
    public String getMessage() {
        return errCode.getMessage();
    }
}

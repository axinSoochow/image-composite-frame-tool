package com.ke.store.vr.common.config.error;


import com.ke.store.vr.common.model.error.StoreApiException;
import com.ke.store.vr.domain.StoreError;

/**
 * @author axin
 * @summary Smp 对外异常断言工具
 */
public interface StoreAssert {

    static void pass(boolean expression, StoreError errCode) {
        if (!expression) {
            justFailed(errCode);
        }
    }

    static void justFailed(StoreError errCode) {
        throw new StoreApiException(errCode);
    }

}

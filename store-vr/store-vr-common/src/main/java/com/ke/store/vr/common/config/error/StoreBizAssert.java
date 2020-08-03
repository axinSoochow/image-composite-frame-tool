package com.ke.store.vr.common.config.error;

import com.ke.store.vr.common.model.error.StoreBusinessException;
import com.ke.store.vr.common.model.error.StoreInnerException;
import com.ke.store.vr.domain.StoreError;
import org.springframework.http.HttpStatus;

/**
 * 内部业务异常断言工具
 */
public interface StoreBizAssert {

    static void validParam(boolean expression, StoreError errCode) {
        if (!expression) {
            justInvalidParam(errCode);
        }
    }

    /**
     * 参数异常
     *
     * @param errCode
     */
    static void justInvalidParam(StoreError errCode) {
        throw new StoreBusinessException(HttpStatus.BAD_REQUEST, errCode.getMessage());
    }

    static void pass(boolean expression, StoreError errCode) {
        if (!expression) {
            justFailed(errCode);
        }
    }

    /**
     * 重载 抛出内部异常
     * @param expression
     * @param inner
     */
    static void pass(boolean expression, StoreInnerException inner) {
        if (!expression) {
            throw inner;
        }
    }

    static void justFailed(StoreError errCode) {
        throw new StoreBusinessException(HttpStatus.FORBIDDEN, errCode.getMessage());
    }
}

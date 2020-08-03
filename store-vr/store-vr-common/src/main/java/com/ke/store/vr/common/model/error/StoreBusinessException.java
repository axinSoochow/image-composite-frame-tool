package com.ke.store.vr.common.model.error;


import com.ke.store.vr.domain.DefaultStoreErrorContext;
import com.ke.store.vr.domain.StoreError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author axin
 * @since 2020-07-03
 * @summary Smp 统一系统业务异常
 */
public class StoreBusinessException extends RuntimeException{

    @Getter
    private StoreError errCode;

    public StoreBusinessException(StoreError errCode) {
        this.errCode = errCode;
    }

    public StoreBusinessException(HttpStatus status, String msg) {
        this.errCode = new DefaultStoreErrorContext(status.value(), msg);
    }

    @Override
    public String getMessage() {
        return errCode.getMessage();
    }
}

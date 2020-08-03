package com.ke.store.vr.domain;


import java.util.function.BiConsumer;
import org.slf4j.Logger;

/**
 * @author axin
 * @since 2020-07-02
 * 打印log日志函数
 */
public enum LogLevel {
    ERROR((logger,err)->{logger.error(err.getMessage(), err);}),
    WARN((logger,err)->{logger.warn(err.getMessage(), err);}),
    INFO((logger,err)->{logger.info(err.getMessage(), err);})
    ;

    private BiConsumer<Logger, RuntimeException> fun;

    LogLevel(BiConsumer<Logger, RuntimeException> fun) {
        this.fun = fun;
    }

    public void log(Logger logger, RuntimeException err){
        fun.accept(logger, err);
    }
}

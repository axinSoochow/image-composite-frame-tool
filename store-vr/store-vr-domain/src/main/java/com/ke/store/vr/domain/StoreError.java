package com.ke.store.vr.domain;

/**
 * @author axin
 * @Summary 统一异基础接口
 */
public interface StoreError {

    int getCode();

    String getMessage();

    default LogLevel getLevel(){
        return LogLevel.ERROR;
    }

    default Object getDetail(){
        return null;
    }

    /**
     * 指定默认的日志等级
     * @param level
     * @return
     */
    default StoreError withLevel(LogLevel level){
        return new DefaultStoreErrorContext(this, level);
    }

    /**
     * 补充错误消息
     * @param extraMsgTemplate
     * @param args
     * @return
     */
    default StoreError withMsg(String msg) {
        String errMsg = this.getMessage() + ":" + msg;
        return new DefaultStoreErrorContext(this, errMsg);
    }

    /**
     * 补充错误消息详情
     * @param extraMsgTemplate
     * @param args
     * @return
     */
    default StoreError withDetail(Object detail){
        return new DefaultStoreErrorContext(this, detail);
    }

    //——————————————————默认实现————————————————————

    StoreError OK = new StoreError() {
        @Override
        public int getCode() {
            return 0;
        }
        @Override
        public String getMessage() {
            return "Ok";
        }
    };

    StoreError 未知 = new StoreError() {
        @Override
        public int getCode() {
            return 999999;
        }

        @Override
        public String getMessage() {
            return "服务器偷懒了";
        }
    };

}

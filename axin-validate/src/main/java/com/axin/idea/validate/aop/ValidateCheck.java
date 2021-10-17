package com.axin.idea.validate.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidateCheck {

    /**
     * 标识接口的key
     * @return
     */
    String key() default "default";

    /**
     * 接口名称
     * @return
     */
    String name() default "接口默认名";
}

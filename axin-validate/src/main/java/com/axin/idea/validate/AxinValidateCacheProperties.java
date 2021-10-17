package com.axin.idea.validate;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "axin.validate.cache")
public class AxinValidateCacheProperties {
    /** 进程缓存开关*/
    private boolean enable = false;
    /** 访问后过期时间，单位秒*/
    private long expireAfterAccess;
    /** 写入后过期时间，单位秒*/
    private long expireAfterWrite = 120;
    /** 写入后刷新时间，单位秒*/
    private long refreshAfterWrite;
    /** 初始化大小,默认50*/
    private int initialCapacity = 50;
    /** 最大缓存对象个数*/
    private long maximumSize = 50;
}

package com.ke.store.vr.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @author axin
 * @description: common 记录接口监控用 线程池
 **/
@Configuration
@EnableAsync
public class StoreCommonThreadPoolConfig {

    /**
     * 异步处理线程核心线程数配置
     */
    @Value("${common.asyn.corePoolSize:8}")
    private Integer asynCorePoolSize;

    /**
     * 异步处理线程最大线程数配置
     */
    @Value("${common.asyn.asynMaximumPoolSize:16}")
    private Integer asynMaximumPoolSize;

    /**
     * 异步处理线程存活时间，单位毫秒
     */
    @Value("${common.asyn.asynKeepAliveTime:60000}")
    private Long asynKeepAliveTime;

    /**
     * 异步处理线程最大队列
     */
    @Value("${common.asyn.asynCapacity:5000}")
    private Integer asynCapacity;


    @Bean("smp-common-thread-pool")
    public ExecutorService executorPools() {
        final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(asynCapacity);
        final String namePattern = "smp-common-thread-pool";
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(namePattern + "-%d").build();
        //用调用者所在的线程来执行任务
        final RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        return new ThreadPoolExecutor(asynCorePoolSize, asynMaximumPoolSize, asynKeepAliveTime, TimeUnit.MILLISECONDS, workQueue, threadFactory, handler);
    }
}

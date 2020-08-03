package com.ke.store.vr.camera.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;

/**
 * redis lua脚本的配置
 *
 * @author huisman (liuhui01@lianjia.com)
 * @copyright (c) 2018, Lianjia Group All Rights Reserved.
 */
@Configuration
public class RedisLuaScriptConfig {
  /** 批量计数器自增 */
  public static final String SCRIPT_RATELIMIT_COUNTER_BATCH = "rateLimitCounterBatch";
  /** 批量计数器检查的逻辑 */
  public static final String SCRIPT_RATELIMIT_COUNTER_BATCH_CHECK = "rateLimitCounterBatchCheck";

  @Bean(name = SCRIPT_RATELIMIT_COUNTER_BATCH)
  public RedisScript<Void> rateLimiterCounterBatchScript() throws IOException {
    ScriptSource scriptSource =
        new ResourceScriptSource(new ClassPathResource("/lua/ratelimit_counter_batch.lua"));
    // null indicates ignoring return result
    return new DefaultRedisScript<>(scriptSource.getScriptAsString(), null);
  }

  @Bean(name = SCRIPT_RATELIMIT_COUNTER_BATCH_CHECK)
  public RedisScript<String> rateLimiterCounterCheckBatchScript() throws IOException {
    ScriptSource scriptSource =
        new ResourceScriptSource(new ClassPathResource("/lua/ratelimit_counter_batch_check.lua"));
    return new DefaultRedisScript<>(scriptSource.getScriptAsString(), String.class);
  }
}

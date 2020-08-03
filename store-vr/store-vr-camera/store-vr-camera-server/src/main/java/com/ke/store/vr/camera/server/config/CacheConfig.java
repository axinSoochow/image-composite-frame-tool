package com.ke.store.vr.camera.server.config;

import com.google.common.collect.ImmutableMap;
import com.ke.ctt.right.autoconfigure.cache.redis.MyRedisCacheManager;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Spring cache的一些配置，建议组件相关配置都放在相应的configuration类中
 *
 * @author
 * @copyright (c) 2017, Lianjia Group All Rights Reserved.
 */
@Configuration
@EnableCaching
public class CacheConfig {
  /**
   * 缓存名，名称暗示了缓存时长，建议格式为： namespace:cache:ttl，比如：demo:cache:2h = 缓存两小时，demo:cache:20m=缓存20分钟
   * </p>
   * 注意： 如果添加了新的缓存名，需要同时在下面的RedisCacheCustomizer#RedisCacheCustomizer里配置名称对应的缓存时长
   * ，时长为0代表永不过期；缓存名最好公司内部唯一，因为可能多个项目共用一个redis。
   *
   * </p>
   * 目前版本的RedisCacheManager生成缓存Key名的策略：cacheName+":"+cacheKey，也就是说默认会将缓存名追加在用户提供的缓存key之前。
   *
   * @see RedisCacheCustomizer#customize(RedisCacheManager)
   */
  public interface CacheNames {
      /** 15分钟缓存组 */
      String CACHE_15MINS = "bk-store-vr:cache:15m";
      /** 30分钟缓存组 */
      String CACHE_30MINS = "bk-store-vr:cache:30m";
      /** 60分钟缓存组 */
      String CACHE_60MINS = "bk-store-vr:cache:60m";
      /** 180分钟缓存组 */
      String CACHE_180MINS = "bk-store-vr:cache:180m";
      /** 12小时缓存组 */
      String CACHE_12h = "bk-store-vr:cache:12h";
  }

  /** cache的一些自定义配置 */
  @Bean
  public RedisCacheCustomizer redisCacheManagerCustomizer() {
    return new RedisCacheCustomizer();
  }

  /* non-public */ static class RedisCacheCustomizer
      implements CacheManagerCustomizer<MyRedisCacheManager> {
    /** CacheManager缓存自定义初始化比较早，尽量不要@autowired 其他spring 组件 */
    @Override
    public void customize(MyRedisCacheManager cacheManager) {
      // 自定义缓存名对应的过期时间
		Map<String, Long> expires = ImmutableMap.<String, Long>builder()
			.put(CacheNames.CACHE_15MINS, TimeUnit.MINUTES.toSeconds(15))
			.put(CacheNames.CACHE_30MINS, TimeUnit.MINUTES.toSeconds(30))
			.put(CacheNames.CACHE_60MINS, TimeUnit.MINUTES.toSeconds(60))
			.put(CacheNames.CACHE_180MINS, TimeUnit.MINUTES.toSeconds(180))
			.put(CacheNames.CACHE_12h, TimeUnit.HOURS.toSeconds(12))
			.build();
      // spring cache是根据cache name查找缓存过期时长的，如果找不到，则使用默认值
      cacheManager.setDefaultExpiration(TimeUnit.MINUTES.toSeconds(30));
      cacheManager.setExpires(expires);
    }
  }
}

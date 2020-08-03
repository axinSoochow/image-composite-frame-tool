package com.ke.store.vr.common.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.ke.store.vr.common.config.redis.FastJsonRedisSerializer;
import com.ke.store.vr.common.config.redis.KeyGeneratorCustomize;
import com.ke.store.vr.common.config.redis.LoggingCacheErrorHandler;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author axin
 * redis 序列化配置
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(RedisConfig.class.getName());

	@Override
	public CacheErrorHandler errorHandler() {
		return new LoggingCacheErrorHandler();
	}

	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGeneratorCustomize("SMP");
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
		log.info("===========================================");
		log.info("=                                         =");
		log.info("=          Smp Reids Config Start         =");
		log.info("=                                         =");
		log.info("===========================================");
		StringRedisTemplate template = new StringRedisTemplate(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new FastJsonRedisSerializer(Object.class));
		template.setHashKeySerializer(new FastJsonRedisSerializer(Object.class));
		template.setHashValueSerializer(new FastJsonRedisSerializer(Object.class));
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		return template;
	}
}

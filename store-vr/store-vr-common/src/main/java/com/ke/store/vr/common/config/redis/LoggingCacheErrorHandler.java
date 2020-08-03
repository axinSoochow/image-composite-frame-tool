package com.ke.store.vr.common.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;

public class LoggingCacheErrorHandler extends SimpleCacheErrorHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		logger.error(String.format("cacheName:%s,cacheKey:%s",
			cache == null ? "unknown" : cache.getName(), key), exception);
		super.handleCacheGetError(exception, cache, key);
	}

	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
									Object value) {
		logger.error(String.format("cacheName:%s,cacheKey:%s",
			cache == null ? "unknown" : cache.getName(), key), exception);
		super.handleCachePutError(exception, cache, key, value);
	}

	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
		logger.error(String.format("cacheName:%s,cacheKey:%s",
			cache == null ? "unknown" : cache.getName(), key), exception);
		super.handleCacheEvictError(exception, cache, key);
	}

	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		logger.error(String.format("cacheName:%s", cache == null ? "unknown" : cache.getName()),
			exception);
		super.handleCacheClearError(exception, cache);
	}
}

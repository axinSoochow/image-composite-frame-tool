package com.ke.cache.rediscaffeinecachestarter.support;

import java.io.Serializable;

public class CacheMessage implements Serializable {

	/** */
	private static final long serialVersionUID = -1L;

	private String cacheName;
	
	private Object key;

	public CacheMessage(String cacheName, Object key) {
		super();
		this.cacheName = cacheName;
		this.key = key;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}
	
}

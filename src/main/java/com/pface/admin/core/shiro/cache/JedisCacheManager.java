/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.pface.admin.core.shiro.cache;

import com.google.common.collect.Sets;
import com.pface.admin.core.utils.JedisUtils;


import com.pface.admin.core.web.Servlets;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * 自定义授权缓存管理类
 * @author ThinkGem
 * @version 2014-7-20
 */

public class JedisCacheManager implements CacheManager {


	private String cacheKeyPrefix = "shiro_cache_";
	
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new JedisCache<K, V>(cacheKeyPrefix + name);
	}

	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}

	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}
	
	/**
	 * 自定义授权缓存管理类
	 * @author ThinkGem
	 * @version 2014-7-20
	 */
	public class JedisCache<K, V> implements Cache<K, V> {

		private Logger logger = LoggerFactory.getLogger(getClass());

		private String redisCacheName = null;

		public JedisCache(String cacheKeyName) {
			this.redisCacheName = cacheKeyName;
//			if (!JedisUtils.exists(cacheKeyName)){
//				Map<String, Object> map = Maps.newHashMap();
//				JedisUtils.setObjectMap(cacheKeyName, map, 60 * 60 * 24);
//			}
//			logger.debug("Init: cacheKeyName {} ", cacheKeyName);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public V get(K key) throws CacheException {
			if (key == null){
				return null;
			}
			
			V v = null;
			HttpServletRequest request = Servlets.getRequest();
			if (request != null){
				v = (V)request.getAttribute(redisCacheName +key);
				if (v != null){
					return v;
				}
			}
			
			V value = null;
			Jedis jedis = null;
			try {
				jedis = JedisUtils.getResource();
				value = (V)JedisUtils.toObject(jedis.hget(JedisUtils.getBytesKey(redisCacheName), JedisUtils.getBytesKey(key)));
				logger.debug("get {} {} {}", redisCacheName, key, request != null ? request.getRequestURI() : "");
			} catch (Exception e) {
				logger.error("get {} {} {}", redisCacheName, key, request != null ? request.getRequestURI() : "", e);
			} finally {
				JedisUtils.returnResource(jedis);
			}
			
			if (request != null && value != null){
				request.setAttribute(redisCacheName +key, value);
			}
			
			return value;
		}

		@Override
		public V put(K key, V value) throws CacheException {
			if (key == null){
				return null;
			}
			
			Jedis jedis = null;
			try {
				jedis = JedisUtils.getResource();
				jedis.hset(JedisUtils.getBytesKey(redisCacheName), JedisUtils.getBytesKey(key), JedisUtils.toBytes(value));
				logger.debug("put {} {} = {}", redisCacheName, key, value);
			} catch (Exception e) {
				logger.error("put {} {}", redisCacheName, key, e);
			} finally {
				JedisUtils.returnResource(jedis);
			}
			return value;
		}

		@SuppressWarnings("unchecked")
		@Override
		public V remove(K key) throws CacheException {
			V value = null;
			Jedis jedis = null;
			try {
				jedis = JedisUtils.getResource();
				value = (V)JedisUtils.toObject(jedis.hget(JedisUtils.getBytesKey(redisCacheName), JedisUtils.getBytesKey(key)));
				jedis.hdel(JedisUtils.getBytesKey(redisCacheName), JedisUtils.getBytesKey(key));
				logger.debug("remove {} {}", redisCacheName, key);
			} catch (Exception e) {
				logger.warn("remove {} {}", redisCacheName, key, e);
			} finally {
				JedisUtils.returnResource(jedis);
			}
			return value;
		}

		@Override
		public void clear() throws CacheException {
			Jedis jedis = null;
			try {
				jedis = JedisUtils.getResource();
				jedis.hdel(JedisUtils.getBytesKey(redisCacheName));
				logger.debug("clear {}", redisCacheName);
			} catch (Exception e) {
				logger.error("clear {}", redisCacheName, e);
			} finally {
				JedisUtils.returnResource(jedis);
			}
		}

		@Override
		public int size() {
			int size = 0;
			Jedis jedis = null;
			try {
				jedis = JedisUtils.getResource();
				size = jedis.hlen(JedisUtils.getBytesKey(redisCacheName)).intValue();
				logger.debug("size {} {} ", redisCacheName, size);
				return size;
			} catch (Exception e) {
				logger.error("clear {}", redisCacheName, e);
			} finally {
				JedisUtils.returnResource(jedis);
			}
			return size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<K> keys() {
			Set<K> keys = Sets.newHashSet();
			Jedis jedis = null;
			try {
				jedis = JedisUtils.getResource();
				Set<byte[]> set = jedis.hkeys(JedisUtils.getBytesKey(redisCacheName));
				for(byte[] key : set){
					Object obj = (K)JedisUtils.getObjectKey(key);
					if (obj != null){
						keys.add((K) obj);
					}
	        	}
				logger.debug("keys {} {} ", redisCacheName, keys);
				return keys;
			} catch (Exception e) {
				logger.error("keys {}", redisCacheName, e);
			} finally {
				JedisUtils.returnResource(jedis);
			}
			return keys;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Collection<V> values() {
			Collection<V> vals = Collections.emptyList();;
			Jedis jedis = null;
			try {
				jedis = JedisUtils.getResource();
				Collection<byte[]> col = jedis.hvals(JedisUtils.getBytesKey(redisCacheName));
				for(byte[] val : col){
					Object obj = JedisUtils.toObject(val);
					if (obj != null){
						vals.add((V) obj);
					}
	        	}
				logger.debug("values {} {} ", redisCacheName, vals);
				return vals;
			} catch (Exception e) {
				logger.error("values {}", redisCacheName, e);
			} finally {
				JedisUtils.returnResource(jedis);
			}
			return vals;
		}
	}
}

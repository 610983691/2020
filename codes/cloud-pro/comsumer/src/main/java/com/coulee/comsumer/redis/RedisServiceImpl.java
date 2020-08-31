package com.coulee.comsumer.redis;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisServiceImpl implements RedisService {

	@Autowired
	RedissonClient red;
	
	@Autowired
	RedisTemplate<String, Object>  redis;
	
	@Override
	@CachePut(value="user",key="'product_test:consumer:user:'+#p0.uid")
	public UserInfo add(UserInfo tobeAdd) {
		log.info("tobeadd {}:",tobeAdd);
		RLock rlock =red.getLock("mylock");
		boolean islock =false;
		try {
			islock = rlock.tryLock(1000, 10000, TimeUnit.MILLISECONDS);
			
			if(islock) {//如果获取到锁，可重入的
			
 				Map<Object, Object> map=redis.opsForHash().entries("mylock");
				for (Entry<Object, Object> item : map.entrySet()) {
					log.info("key ={},val={}",item.getKey(),item.getValue());
				}
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			log.info("获取分布式锁失败：",e);
		}finally {
			rlock.unlock();//释放锁
		}
		return tobeAdd;
	}

	@Override
	@CacheEvict(value="user",key="'product_test:consumer:user:'+#p0.uid")
	public UserInfo update(UserInfo tobeUpdate) {
		return tobeUpdate;
	}

	@Override
	@CacheEvict(value="user",key="'product_test:consumer:user:'+#p0")
	public UserInfo del(String uid) {
		return null;
	}

	@Override
	@Cacheable(value="user",key="'product_test:consumer:user:'+#p0")
	public UserInfo find(String uid) {
		log.info("执行了吗？ {}:",uid);
		return null;
	}


}

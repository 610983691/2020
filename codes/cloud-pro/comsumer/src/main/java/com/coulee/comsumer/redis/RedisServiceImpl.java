package com.coulee.comsumer.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisServiceImpl implements RedisService {

	@Override
	@CachePut(value="user",key="'product_test:consumer:user:'+#p0.uid")
	public UserInfo add(UserInfo tobeAdd) {
		log.info("tobeadd {}:",tobeAdd);
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

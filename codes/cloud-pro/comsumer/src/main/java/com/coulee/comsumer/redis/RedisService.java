package com.coulee.comsumer.redis;

public interface RedisService {

	UserInfo add(UserInfo tobeAdd);
	UserInfo update(UserInfo tobeUpdate);
	UserInfo del(String uid);
	UserInfo find(String uid );
}

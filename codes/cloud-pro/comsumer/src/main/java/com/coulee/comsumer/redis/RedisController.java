package com.coulee.comsumer.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

	@Autowired
	RedisService redis;
	
	@RequestMapping("/redis/getuser/{uid}")
	@ResponseBody
	public UserInfo get(@PathVariable("uid")String uid) {
		return redis.find(uid);
	}
	@RequestMapping("/redis/deluser/{uid}")
	public UserInfo del(@PathVariable("uid")String uid) {
		return redis.del(uid);
	}
	@RequestMapping("/redis/adduser")
	public UserInfo adduser() {
		return redis.add(new UserInfo());
	}
	@RequestMapping("/redis/updateuser/{uid}")
	public UserInfo updateuser(@PathVariable("uid")String uid) {
		UserInfo u = new UserInfo() ;
		u.setUname("username update");
		u.setUid(uid);
		return redis.update(u);
	}
}

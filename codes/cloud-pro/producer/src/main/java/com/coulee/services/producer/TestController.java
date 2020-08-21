package com.coulee.services.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {
	
	@Value("${test_client}")
	private String test_client;
	@Value("${db.name}")
	private String dbname;
	
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping("/local")
	public String local() {
		return getMsg();
	}
	
	@RequestMapping("/echo")
	public String echo() {
		return getMsg() +"\n config is："+test_client +", share.db db.name config is :"+dbname;
	}
	private String getMsg() {
		String rst = context.getEnvironment().getProperty("spring.application.name")+":"+context.getEnvironment().getProperty("local.server.port");
		return rst;
	}
	
	@RequestMapping("/random")
	public String random() {
		if(System.currentTimeMillis()%4 == 0) {//随机来看4次有一次返回错误
			throw new RuntimeException(getMsg()+"故意抛出业务异常");
		}
		return getMsg() +"\n";
	}
	
	@RequestMapping("/feign")
	public String feign() {
		return getMsg() +"/feign";
	}
	
	@RequestMapping("/feignerr")
	public String feignerr() {
		if(System.currentTimeMillis()%4 == 0) {//随机来看4次有一次返回错误
			throw new RuntimeException(getMsg()+",/feignerr 故意抛出异常");
		}
		return getMsg() +"/feignerr";
	}
}

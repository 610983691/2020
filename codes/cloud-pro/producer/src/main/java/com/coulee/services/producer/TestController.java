package com.coulee.services.producer;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@Value("${test_client}")
	private String test_client;
	@Value("${db.name}")
	private String dbname;
	
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping("/echo")
	public String echo() {
		return "hello ,im provider:"+ context.getEnvironment().getProperty("local.server.port") +"\n config is："+test_client +", share.db db.name config is :"+dbname;
	}
	
	@RequestMapping("/random")
	public String random() {
		if(System.currentTimeMillis()%4 == 0) {//随机来看4次有一次返回错误
			throw new RuntimeException("故意抛出业务异常");
		}
		return "hello ,im provider:"+ context.getEnvironment().getProperty("local.server.port") +"\n";
	}
	
}

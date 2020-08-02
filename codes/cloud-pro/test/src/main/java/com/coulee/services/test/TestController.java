package com.coulee.services.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@Value("${test_client}")
	private String test_client;
	@Value("${db.name}")
	private String dbname;
	
	@RequestMapping("/echo")
	public String echo(@RequestParam("param")String param) {
		return "hello ,im test client222222=>"+ param +"\n config is："+test_client +", share.db db.name config is :"+dbname;
	}
}

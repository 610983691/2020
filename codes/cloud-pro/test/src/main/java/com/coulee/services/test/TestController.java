package com.coulee.services.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

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
	
	@RequestMapping("/hello")
	public String hello() {
		return service.sayHello("hello");
	}
	
	@Autowired
    private TestService service;
	@Service
	public class TestService {

	    @SentinelResource(value = "hello")
	    public String sayHello(String name) {
	        return "Hello, " + name;
	    }
	}
}

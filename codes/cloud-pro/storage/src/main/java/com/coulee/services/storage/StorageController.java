package com.coulee.services.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/storage")
public class StorageController {
	
	
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping("/deduct")
	public String create() {
		log.info("库存扣除：{}");
		return getMsg()+"-> 库存扣除。";
	}
	
	
	private String getMsg() {
		String rst = context.getEnvironment().getProperty("spring.application.name")+":"+context.getEnvironment().getProperty("local.server.port");
		return rst;
	}
	
}

package com.coulee.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderControlle {
	
	
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping("/create")
	public String create() {
		log.info("创建订单：{}");
		return getMsg();
	}
	
	@RequestMapping("/cancel/{id}")
	public String cancel(@PathVariable("id") Long id) {
		log.info("用户取消订单：{}",id);
		return getMsg() +"->cancel order id ."+id;
	}
	
	
	private String getMsg() {
		String rst = context.getEnvironment().getProperty("spring.application.name")+":"+context.getEnvironment().getProperty("local.server.port");
		return rst;
	}
	
}

package com.coulee.cloud.gateway;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

	
	@RequestMapping("/echo")
	public String echo() {
		return "echo:"+LocalDateTime.now();
	}
	
}

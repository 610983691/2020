package com.coulee.comsumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.coulee.comsumer.dao")
public class ComsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComsumerApplication.class, args);
	}

}

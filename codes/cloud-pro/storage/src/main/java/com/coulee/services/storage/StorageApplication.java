package com.coulee.services.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用于测试分布式事务的库存服务
 * @author zitong
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class StorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageApplication.class, args);
	}
	
}

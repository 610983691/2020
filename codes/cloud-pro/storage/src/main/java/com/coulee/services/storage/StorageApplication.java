package com.coulee.services.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用于测试分布式事务的库存服务
 * @author zitong
 *
 */
@SpringBootApplication(scanBasePackages = {"com.coulee.cloud.common", "com.coulee.services.storage"}) // 必须扫描common的配置包
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.coulee.services.storage.dao")
public class StorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageApplication.class, args);
	}
	
}

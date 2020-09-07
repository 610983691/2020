package com.coulee.services.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用于测试分布式事务的订单服务
 * 
 * @author zitong
 *
 */
@SpringBootApplication(scanBasePackages = {"com.coulee.cloud.common", "com.coulee.services.order"}) // 必须扫描common的配置包
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.coulee.services.order.dao")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}

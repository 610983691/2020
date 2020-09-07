package com.coulee.cloud.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// 由于指定的scanbasepackages，所以当前的包也需要扫描
@SpringBootApplication(scanBasePackages = {"com.coulee.cloud.common", "com.coulee.cloud.example"}) // 必须扫描common的配置包
@EnableFeignClients // 开启feign做远程调用
@EnableDiscoveryClient // 开启服务注册与发现
// @MapperScan("com.coulee.comsumer.dao")//
@EnableTransactionManagement
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}

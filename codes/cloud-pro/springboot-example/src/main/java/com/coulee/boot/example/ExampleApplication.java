package com.coulee.boot.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.FilterType;

import com.coulee.boot.example.config.StandaloneDataSourceConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

// 由于指定的scanbasepackages，所以当前的包也需要扫描
@SpringBootApplication(scanBasePackages = {"com.coulee.cloud.core", "com.coulee.boot.example"},exclude= {DataSourceAutoConfiguration.class}) // 必须扫描common的配置包
@ComponentScan(excludeFilters= {@Filter(type=FilterType.ASSIGNABLE_TYPE,classes= {StandaloneDataSourceConfig.class})})
// @MapperScan("com.coulee.comsumer.dao")//
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}

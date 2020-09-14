package com.coulee.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	
	/**
	 * 如果不启用nacosRule默认就是轮询实现。
	 * 启用原型模式，否则网关会路由错误到producer上。
	 * 解决方法是参考：
	 * <a href="https://github.com/alibaba/spring-cloud-alibaba/issues/1184">issue</a>
	 */
		@Bean
		@Scope("prototype")
        public IRule loadBalanceRule(){
          return new NacosRule();
        }

}

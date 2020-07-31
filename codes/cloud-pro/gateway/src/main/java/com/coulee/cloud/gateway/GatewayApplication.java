package com.coulee.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext =SpringApplication.run(GatewayApplication.class, args);
		while(true) {
			 System.out.println("=======================" );
			String userName = applicationContext.getEnvironment().getProperty("DISABLE_REFRESH_GROUP");
			 System.out.println("DISABLE_REFRESH_GROUP :" +userName);
			 String REFRESH_GROUP = applicationContext.getEnvironment().getProperty("REFRESH_GROUP");
			 System.out.println("REFRESH_GROUP :" +REFRESH_GROUP);
			 String REFRESH_GROUP_PRODUCT_SHARED = applicationContext.getEnvironment().getProperty("db.name");
			 System.out.println("REFRESH_GROUP_PRODUCT_SHARED :" +REFRESH_GROUP_PRODUCT_SHARED);
			 try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}

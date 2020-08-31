package com.coulee.comsumer.config;

import java.io.IOException;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.alibaba.cloud.nacos.NacosPropertySourceRepository;

@Configuration
public class RedissonSpringDataConfigStandalone {

//   @Bean
//   public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
//       return new RedissonConnectionFactory(redisson);
//   }

   @Bean(destroyMethod = "shutdown")
   public RedissonClient redisson() throws IOException {
	   //这里应该是从nacos中获取redis配置
//	   Map<String,Object> pro = NacosPropertySourceRepository.getNacosPropertySource("product_test_shared_db.properties","REFRESH_GROUP_PRODUCT_SHARED").getSource();
       Config config = new Config();
       config.useSingleServer().setAddress("redis://127.0.0.1:6379");
       return Redisson.create(config);
   }

}

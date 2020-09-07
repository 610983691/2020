package com.coulee.cloud.common.config;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 分布式锁连接配置
 * 
 * @author tongjie
 *
 */
@Configuration
public class RedissonSpringDataConfigStandalone {

    // @Bean
    // public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
    // return new RedissonConnectionFactory(redisson);
    // }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        // 这里应该是从nacos中获取redis配置
        // Map<String,Object> pro =
        // NacosPropertySourceRepository.getNacosPropertySource("product_test_shared_db.properties","REFRESH_GROUP_PRODUCT_SHARED").getSource();
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        config.setNettyThreads(0);// 不需要默认的32个线程
        return Redisson.create(config);
    }

}

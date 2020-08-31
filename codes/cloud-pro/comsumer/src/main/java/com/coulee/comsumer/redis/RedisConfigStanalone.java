package com.coulee.comsumer.redis;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfigStanalone extends CachingConfigurerSupport{

	/***
	 * 创建连接工厂，
	 * TODO:配置应该从配置文件读
	 * @return
	 */
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379));
	}

	/***
	 * 获取redistemplate
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());//单独设置keySerializer
		template.setValueSerializer(new GenericFastJsonRedisSerializer());//单独设置valueSerializer
		return template;
	}

	 // 缓存管理器管理的缓存都需要有对应的缓存空间，否则抛异常：No cache could be resolved for 'Builder...
	//这个方法如果不设置，那么通过@cache等注解的value不会以json方式存入redis.
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    	  RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
          // 设置缓存管理器管理的缓存的默认过期时间
          defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofSeconds(300))//5分钟
                  // 设置 key为string序列化
                  .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                  // 设置value为json序列化
                  .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()))
                  // 不缓存空值
                  .disableCachingNullValues();

          RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                  .cacheDefaults(defaultCacheConfig)
                  .build();
          return cacheManager;
    }
}

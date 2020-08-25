package com.coulee.comsumer.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfigMasterSlave {

	@Bean
	  public LettuceConnectionFactory redisConnectionFactory() {

	    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
	      .readFrom(io.lettuce.core.ReadFrom.REPLICA_PREFERRED)
	      .build();

	    RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration("server", 6379);

	    return new LettuceConnectionFactory(serverConfig, clientConfig);
	  }
}

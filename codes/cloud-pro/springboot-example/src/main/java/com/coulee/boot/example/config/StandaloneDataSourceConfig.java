package com.coulee.boot.example.config;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于配置单数据源的启动，让系统开始就启动数据库连接。
 * 
 * @author tongjie
 *
 */
@Configuration
@Slf4j
public class StandaloneDataSourceConfig {

    @Bean
    public DataSource hikariDataSource() {
        log.info("配置数据源");
        HikariConfig config = new HikariConfig();
        log.info("解密密码：{}",config.getPassword());
        config.setPassword(config.getPassword());
        return new HikariDataSource(config);
    }
}

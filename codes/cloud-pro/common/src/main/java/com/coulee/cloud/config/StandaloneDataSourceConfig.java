package com.coulee.cloud.config;

import java.util.Map;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.alibaba.cloud.nacos.NacosPropertySourceRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.util.PropertyElf;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于配置单数据源的启动，让系统开始就启动数据库连接。
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
        Map<String,Object> pro = NacosPropertySourceRepository.getNacosPropertySource("product_test_shared_db.properties","REFRESH_GROUP_PRODUCT_SHARED").getSource();
        String afterDecode= (String)pro.get("password");//TODO:获取密码，然后解密
        log.info("解密数据库密码:{}",afterDecode);
        pro.put("password",afterDecode);//解密后放回去
        pro.entrySet().forEach(action->{
        	if(!action.getKey().equals("password")) {
        		log.info("datasource config put:{}--{}",action.getKey(),action.getValue());
        	}
        	config.addDataSourceProperty(action.getKey(), action.getValue());
        });
        PropertyElf.setTargetFromProperties(config, config.getDataSourceProperties());
        return new HikariDataSource(config);
    }
}

package com.coulee.cloud.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;

/**
 * 如果使用分布式事务就需要该配置，否则应该在exclude中指定当前配置类
 * 0.9.0开始支持自动代理数据源
 * @author tongjie
 *
 */
//@Configuration
@Slf4j
public class DataSourceProxyConfig {



    @Bean
    public DataSourceProxy dataSourceProxy(@Autowired DataSource dataSource) {
    	log.info("注入数据源代理到seata：{}",dataSource);
        return new DataSourceProxy(dataSource);
    }

    /***
     * mybatis需要额外注入
     * 参考：
     * https://github.com/seata/seata-samples/blob/master/doc/quick-integration-with-spring-cloud.md
     * @param dataSourceProxy
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSourceProxy dataSourceProxy) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSourceProxy);
        return sqlSessionFactoryBean.getObject();
    }
}

package com.coulee.comsumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.extern.slf4j.Slf4j;
@Configuration
@Slf4j
public class SentinelNacosDatasourceBean {

	@Value("${spring.cloud.sentinel.datasource.flow.nacos.server-addr}")
	private String remoteAddress;
	@Value("${spring.cloud.sentinel.datasource.flow.nacos.groupId}")
	private String groupId;
	@Value("${spring.cloud.sentinel.datasource.flow.nacos.dataId}")
	private String dataId;
	
	@Bean(initMethod="init")
	public SentinelNacosDatasource sentinelNacosDatasource(){
		return new SentinelNacosDatasource();
	}
	
	class SentinelNacosDatasource{
		public void init() {
			log.info("开始加载nacos配置");
			// remoteAddress 代表 Nacos 服务端的地址
			// groupId 和 dataId 对应 Nacos 中相应配置
			ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, dataId,
			    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
			FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
		}
	}
	
}

package com.coulee.cloud.gateway;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SentinelNacosDatasourceBean {

	/***
	 * 流控规则
	 */
	public static final String FLOW_RULE_DATA_ID_POSTFIX = "-flow-rules.json";

	/***
	 * 系统规则
	 */
	public static final String SYSTEM_RULE_DATA_ID_POSTFIX = "-system-rules.json";

	/***
	 * 降级规则
	 */
	public static final String DEGRADE_RULE_DATA_ID_POSTFIX = "-degrade-rules.json";


	public static final String SENTINEL_GROUP = "SENTINEL_GROUP";

	@Value("${spring.cloud.sentinel.datasource.nacos.server-addr}")
	private String remoteAddress;

	@Value("${spring.cloud.sentinel.datasource.nacos.namespace}")
	private String namespace;

	@Value("${spring.application.name}")
	private String appname;

	@Bean(initMethod = "init")
	public SentinelNacosDatasource sentinelNacosDatasource() {
		return new SentinelNacosDatasource();
	}

	class SentinelNacosDatasource {
		public void init() {
			log.info("开始加载nacos配置");
			Properties properties = new Properties();
			properties.put(PropertyKeyConst.SERVER_ADDR, remoteAddress);
			properties.put(PropertyKeyConst.NAMESPACE, namespace);

			// remoteAddress 代表 Nacos 服务端的地址
			// groupId 和 dataId 对应 Nacos 中相应配置
			// 注册限流规则
			ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(properties,
					SENTINEL_GROUP, appname + FLOW_RULE_DATA_ID_POSTFIX,
					source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
					}));
			FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

			
			// 注册降级规则
			ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new NacosDataSource<>(properties,
					SENTINEL_GROUP, appname + DEGRADE_RULE_DATA_ID_POSTFIX,
					source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
					}));
			DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
			// 注册系统规则
			ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new NacosDataSource<>(properties,
					SENTINEL_GROUP, appname + SYSTEM_RULE_DATA_ID_POSTFIX,
					source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {
					}));
			SystemRuleManager.register2Property(systemRuleDataSource.getProperty());

		}
	}

}

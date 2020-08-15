package com.coulee.services.producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

import lombok.extern.slf4j.Slf4j;

/***
 * 该类的作用就是自动加载一个系统限流规则
 * @author zitong
 *
 */
@Slf4j
@Configuration
public class SentinelConfig {
	
	@Bean
	public SentinelConfig.SysRule sysRule() {
		return new SentinelConfig.SysRule();
	}
	
	
	static class SysRule {
		/**
		 * 系统自适应的限流规则
		 */
		private   SysRule() {
			log.info("系统自适应限流规则添加");
	        List<SystemRule> rules = new ArrayList<SystemRule>();
	        SystemRule rule = new SystemRule();
	        // max load is 3
	        rule.setHighestSystemLoad(3.0);
	        // max cpu usage is 60%
	        rule.setHighestCpuUsage(0.6);
	        // max avg rt of all request is 10 ms
	        rule.setAvgRt(100);
	        // max total qps is 20
	        rule.setQps(20);
	        // max parallel working thread is 10
	        rule.setMaxThread(10);

	        rules.add(rule);
	        SystemRuleManager.loadRules(Collections.singletonList(rule));
	    }
	}
	
}

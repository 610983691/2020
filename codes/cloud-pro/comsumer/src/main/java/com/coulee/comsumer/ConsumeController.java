package com.coulee.comsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ConsumeController {

	@Autowired
	private ProducerService producer;
	
	@RequestMapping("/local")
	public String local() {
		String rst =  "local====";
		return rst;
	}
	
	
	/***
	 * 需要注意的是：远程调用方式和sentinel无关，远程调用还是可以用springcloud 推荐的feign或者resttemplate方式。
	 * 如果相关了,那这个sentinel的耦合就太高了。
	 * @return
	 */
	@RequestMapping("/remoteecho")
	@SentinelResource(fallback = "fallback",blockHandler ="blockHandler")
	public String remoteInvoke() {
		String rst =  "remote===="+producer.echo();
		return rst;
	}
	
	@SentinelResource(fallback = "fallback",blockHandler ="blockHandler")
	@RequestMapping("/remoterandom")
	public String remoterandom() {
		return "remote===="+producer.random();
	}
	
	/***
	 * 这里的远端服务异常，有可能是远端抛出异常，也可能是远端的sentinel。
	 * @param throwable
	 * @return
	 */
	// 异常回退
    public String fallback(Throwable throwable) {
    	log.warn("【进入fallback方法】，远端服务异常（远端可能是被降级或者错误，通过测试echo方法可以发现，echo方法只会被限流而不会抛出错误。）", throwable);
        return "【进入fallback方法】，远端服务异常";
    }

    // sentinel回退
    public String blockHandler(BlockException e) {
    	log.warn("【进入blockHandler方法】,限流，请稍后再试", e);
        return "【进入blockHandler方法】,限流，请稍后再试";
    }
}

package com.coulee.comsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ConsumeController {

	@Autowired
	private ProducerService producer;

	/**
	 * consumer的本地调用
	 * 
	 * @return
	 */
	@RequestMapping("/local")
	public String local() {
		String rst = "local====";
		return rst;
	}

	/***
	 * 需要注意的是：远程调用方式和sentinel无关，远程调用还是可以用springcloud 推荐的feign或者resttemplate方式。
	 * 如果相关了,那这个sentinel的耦合就太高了。 这个方法远端不会抛业务异常，只会抛限流异常
	 * 
	 * @return
	 */
	@RequestMapping("/remoteecho")
	@SentinelResource(value = "remoteecho2", fallback = "fallback", blockHandler = "blockHandler")
	public String remoteInvoke() {
		String rst = "remote====" + producer.echo();
		return rst;
	}

	/**
	 * 这个方法远端会抛业务异常，也会抛限流异常
	 */
	@SentinelResource(fallback = "fallback", blockHandler = "blockHandler")
	@RequestMapping("/remoterandom")
	public String remoterandom() {
		String rst = "remote====" + producer.random();
		return rst;
	}

	/***
	 * 这里的远端服务异常，有可能是远端抛出异常，也可能是远端的sentinel。
	 * 
	 * @param throwable
	 * @return
	 */
	public String fallback(Throwable throwable) {
		if (BlockException.isBlockException(throwable)) {
			log.warn("这个是一个block异常", throwable);
			if (throwable instanceof DegradeException) {
				log.warn("这个是一个degraede异常", throwable);
				return "【进入fallback方法】，远端服务degraede异常错误！";
			}
			return "【进入fallback方法】，远端服务block异常错误！";
		} else {
			log.warn("远端业务异常", throwable);
			return "【进入fallback方法】，远端业务服务错误！";
		}
	}

	/**
	 * 若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler
	 * 处理逻辑。
	 * 
	 * @param e
	 * @return
	 */
	// sentinel降级
	public String blockHandler(BlockException e) {
		log.warn("【进入blockHandler方法】这个是一个block异常，因为远端被限流或降级", e);
		if (e instanceof DegradeException) {
			log.warn("这个是一个degraede异常", e);
			return "【进入blockHandler方法】，远端服务degraede异常错误！";
		}
		return "【进入blockHandler方法】,远端被限流，请稍后再试";
	}
}

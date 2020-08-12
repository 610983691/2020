package com.coulee.comsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@Controller
public class ConsumeController {

	@Autowired
	private ProducerService producer;
	/***
	 * 需要注意的是：远程调用方式和sentinel无关，远程调用还是可以用springcloud 推荐的feign或者resttemplate方式。
	 * 如果相关了,那这个sentinel的耦合就太高了。
	 * @return
	 */
	@RequestMapping("/remoteecho")
	public String remoteInvoke() {
		return "remote===="+producer.echo();
	}
	
	@SentinelResource(fallback = "fallback",blockHandler ="blockHandler")
	@RequestMapping("/remoterandom")
	public String remoterandom() {
		return "remote===="+producer.random();
	}
	
	// 异常回退
    public String fallback(Throwable throwable) {
        return String.format("【进入fallback方法】购买%d份%s失败，%s",  throwable.getMessage());
    }

    // sentinel回退
    public String blockHandler(BlockException e) {
        return String.format("【进入blockHandler方法】购买%d份%s失败，当前购买人数过多，请稍后再试");
    }
}

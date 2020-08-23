package com.coulee.services.producer;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

//@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

	/***
	 * 处理限流异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BlockException.class)
	@ResponseBody
	public String sentinelBlockHandler(BlockException e) {
		log.info("限流或者熔断", e);
		return JSONObject.toJSONString(new Msg());
	}

	@Data
	class Msg {
		int code = -1;

		String msg = "当前访问人数过多，请稍后再试。";
	}
}

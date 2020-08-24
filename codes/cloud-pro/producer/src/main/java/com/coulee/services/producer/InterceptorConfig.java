package com.coulee.services.producer;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.config.SentinelWebMvcConfig;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.util.StringUtil;

import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	ApplicationContext  app;
	/**
	 * 方式一、注入自己实现的限流异常处理，自己的处理就是不处理抛出异常，然后交给外层的controller异常处理器统一进行处理
	 * 方拾二、直接这里拦截返回页面429status,+msg.
	 * 
	 * 这里先采用方式2.方式1也测试过，需要搭配controlleradvice做异常拦截，测试也OK。
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		SentinelWebMvcConfig config = app.getBean(SentinelWebMvcConfig.class);
		config.setBlockExceptionHandler((request, response, e) -> {
			if(e instanceof DegradeException) {
				// Return 429 (Too Many Requests) by default.
		        StringBuffer url = request.getRequestURL();
		        if ("GET".equals(request.getMethod()) && StringUtil.isNotBlank(request.getQueryString())) {
		            url.append("?").append(request.getQueryString());
		        }
		        log.warn("接口被熔断：{},e{}",url,e.getRule());
		        response.setStatus( HttpStatus.SERVICE_UNAVAILABLE.value());
		        PrintWriter out = response.getWriter();
		        out.print(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());//降级提示
		        out.flush();
		        out.close();
			}else {
				// Return 429 (Too Many Requests) by default.
		        StringBuffer url = request.getRequestURL();
		        if ("GET".equals(request.getMethod()) && StringUtil.isNotBlank(request.getQueryString())) {
		            url.append("?").append(request.getQueryString());
		        }
		        log.warn("接口被限流：{}",url);
		        response.setStatus( HttpStatus.TOO_MANY_REQUESTS.value());
		        PrintWriter out = response.getWriter();
		        out.print(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());//限流提示
		        out.flush();
		        out.close();
			}
		    });
	}
}
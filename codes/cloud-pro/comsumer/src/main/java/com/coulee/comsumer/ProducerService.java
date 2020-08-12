package com.coulee.comsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("producer")
public interface ProducerService {

	/***
	 * 访问producer random接口
	 * @return
	 */
	@RequestMapping("/random")
	public String random() ;
	
	
	/***
	 * 访问producer echo接口
	 * @return
	 */
	@RequestMapping("/echo")
	public String echo() ;
}

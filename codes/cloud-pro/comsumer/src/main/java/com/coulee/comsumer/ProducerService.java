package com.coulee.comsumer;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value="producer",fallback=ProducerService2Fallback.class,configuration = FeignConfiguration2.class)
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
	
	/***
	 * 访问producer feign接口
	 * @return
	 */
	@RequestMapping("/feign")
	public String feign() ;
	
	/***
	 * 访问producer feignerr接口
	 * @return
	 */
	@RequestMapping("/feignerr")
	public String feignerr() ;
}
class FeignConfiguration2 {
    @Bean
    public ProducerService2Fallback producerService2Fallback() {
        return new ProducerService2Fallback();
    }
}
class ProducerService2Fallback implements ProducerService {

	@Override
	public String feign() {
		// TODO Auto-generated method stub
		return "feign custom fallback";
	}

	@Override
	public String feignerr() {
		// TODO Auto-generated method stub
		return "feignerr custom fallback";
	}

	@Override
	public String random() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String echo() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
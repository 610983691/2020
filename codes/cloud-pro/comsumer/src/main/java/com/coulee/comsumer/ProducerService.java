package com.coulee.comsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;

import feign.FeignException;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;


@FeignClient(value="producer",fallbackFactory = ProducerServiceFallbackFactory.class)
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
	@RequestMapping(value="/timeout",method = RequestMethod.GET)
	public String timeout() ;
}
@Component
@Slf4j
 class ProducerServiceFallbackFactory implements FallbackFactory<ProducerServiceFallback> {
    @Override
    public ProducerServiceFallback create(Throwable throwable) {
    	log.warn("远端异常",throwable);
        return new ProducerServiceFallback(throwable);
    }
}
@Slf4j
 class ProducerServiceFallback implements ProducerService {
    private Throwable throwable;

    ProducerServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    
	@Override
	public String random() {
		if(throwable instanceof FeignException) {
			FeignException fe =(FeignException)throwable;
			if(fe.status()==HttpStatus.TOO_MANY_REQUESTS.value()) {
				return "远端random限流，请稍后再试！";
			}else if(fe.status()==HttpStatus.SERVICE_UNAVAILABLE.value()) {
				return "远端random熔断，服务不可用，请稍后再试！";
			}
			log.warn("random处理",throwable);
		}else if(throwable instanceof DegradeException) {//熔断异常特殊处理
			return "consumer端主动熔断远端random接口，服务不可用，请稍后再试！";
		}else if(throwable instanceof BlockException) {
			return "consumer主动限流远端random服务，请稍后再试！";
		}
		
		return "远端random业务异常";
	}

	@Override
	public String echo() {
		log.warn("echo处理",throwable);
		return "远端echo异常";
	}

	@Override
	public String feign() {
		FeignException fe =(FeignException)throwable;
		if(fe.status()==HttpStatus.TOO_MANY_REQUESTS.value()) {
			return "远端feign限流，请稍后再试！";
		}else if(fe.status()==HttpStatus.SERVICE_UNAVAILABLE.value()) {
			return "远端feign熔断，服务不可用，请稍后再试！";
		}
		log.warn("feign处理",throwable);
		return "远端feign业务异常";
	}

	@Override
	public String timeout() {
		if(throwable instanceof FeignException) {
			FeignException fe =(FeignException)throwable;
			if(fe.status()==HttpStatus.TOO_MANY_REQUESTS.value()) {
				return "远端timeout限流，请稍后再试！";
			}else if(fe.status()==HttpStatus.SERVICE_UNAVAILABLE.value()) {
				return "远端timeout熔断，服务不可用，请稍后再试！";
			}
			log.warn("timeout处理",throwable);
		}else if(throwable instanceof DegradeException) {//熔断异常特殊处理
			return "consumer端主动熔断远端timeout接口，服务不可用，请稍后再试！";
		}else if(throwable instanceof BlockException) {
			return "consumer主动限流远端timeout服务，请稍后再试！";
		}
		
		return "远端timeout业务异常";
	}

}
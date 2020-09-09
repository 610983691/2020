package com.coulee.services.order.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;

import feign.FeignException;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;


@FeignClient(value="storage",fallbackFactory = StorageServiceFallbackFactory.class)
@RequestMapping("/storage")
public interface StorageService {

	/***
	 * 扣除库存
	 * @param storageId
	 * @param amount
	 * @return
	 */
	@RequestMapping("/deduct/{storageId}/{amount}")
	public String deduct(@PathVariable("storageId") Long storageId,@PathVariable("amount")Long amount);
	
}
@Component
@Slf4j
 class StorageServiceFallbackFactory implements FallbackFactory<StorageServiceFallback> {
    @Override
    public StorageServiceFallback create(Throwable throwable) {
    	log.warn("扣除库存失败",throwable);
        return new StorageServiceFallback(throwable);
    }
}
@Slf4j
 class StorageServiceFallback implements StorageService {
    private Throwable throwable;

    StorageServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }

	@Override
	public String deduct(Long storageId, Long amount) {
		log.error("有异常信息",throwable);
		if(throwable instanceof FeignException) {
			FeignException fe =(FeignException)throwable;
			if(fe.status()==HttpStatus.TOO_MANY_REQUESTS.value()) {
				log.error( "远端random限流，请稍后再试！");
			}else if(fe.status()==HttpStatus.SERVICE_UNAVAILABLE.value()) {
				log.error( "远端random熔断，服务不可用，请稍后再试！");
			}
			log.warn("random处理",throwable);
		}else if(throwable instanceof DegradeException) {//熔断异常特殊处理
			log.error( "consumer端主动熔断远端random接口，服务不可用，请稍后再试！");
		}else if(throwable instanceof BlockException) {
			log.error("consumer主动限流远端random服务，请稍后再试！");
		}
		throw new IllegalStateException(throwable);//包装异常并抛出
	}


}
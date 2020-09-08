package com.coulee.services.storage.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties.Storage;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.services.storage.dao.StorageMapper;
import com.coulee.services.storage.entity.StorageEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/storage")
public class StorageController {
	
	
	@Autowired
	private StorageMapper storageMapper;
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping("/deduct/{storageId}/{amount}")
	public String deduct(@PathVariable("storageId") Long storageId,@PathVariable("amount")Long amount) {
		log.info("库存扣除：{}");
		StorageEntity storage = new StorageEntity();
		storage.setId(storageId);
		StorageEntity currentStorage = storageMapper.selectById(storageId);//分布式锁需要，否则两个线程同时查，都有库存，结果同时扣除成功。保证一个扣除的时候，另一个无法扣除库存
		storage.setAmount(currentStorage.getAmount() - amount);//这里可能为负
		storage.setUpdateAt(LocalDateTime.now());
		storageMapper.updateById(storage);
		return getMsg()+"-> 库存扣除。";
	}
	
	
	private String getMsg() {
		String rst = context.getEnvironment().getProperty("spring.application.name")+":"+context.getEnvironment().getProperty("local.server.port");
		return rst;
	}
	
}

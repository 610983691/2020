package com.coulee.comsumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.comsumer.common.SnowFlake;
import com.coulee.comsumer.dao.OrgMapper;
import com.coulee.comsumer.dao.UserMapper;
import com.coulee.comsumer.entity.OrgEntity;
import com.coulee.comsumer.entity.UserEntity;
import com.coulee.comsumer.service.UserService;
import com.coulee.comsumer.vo.UserListVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	UserMapper userMapper;
	@Autowired
	OrgMapper orgMapper;
	

	@Autowired
	SnowFlake snowFlakeId;
	
	@RequestMapping("/user/list")
	public List<UserEntity> findUser(){
		List<UserEntity> users = userMapper.selectList(null);
		return users;
	}
	
	@RequestMapping("/user/create")
	public UserEntity createUser(@RequestBody UserEntity us){
		us.setId(snowFlakeId.nextId());
		 userMapper.insert(us);
		return us;
	}
	
	@Transactional(rollbackFor = Exception.class)//这里测试使用默认配置即可
	@RequestMapping("/user/update")
	public UserEntity update(@RequestBody UserEntity us){
		UserEntity oldus = userMapper.selectById(us.getId());
		us.setUsername(oldus.getUsername()+"modify");
		userMapper.updateById(us);//抛异常
		UserEntity newusr = userMapper.selectById(us.getId());
		log.info("new user info :{}",newusr);//打印新查询的user
		int a=1/0;//抛异常,
		return us;
	}
	
	@RequestMapping("/user/{id}")
	public UserEntity findById(@PathVariable Long id){
		UserEntity us = userMapper.selectById(id);
		return us;
	}
	
}

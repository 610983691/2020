package com.coulee.comsumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.comsumer.common.SnowFlake;
import com.coulee.comsumer.dao.UserMapper;
import com.coulee.comsumer.entity.UserEntity;
import com.coulee.comsumer.service.UserService;
import com.coulee.comsumer.vo.UserListVO;

@RestController
public class UserController {

	@Autowired
	UserMapper userMapper;
	

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
	
	@RequestMapping("/user/update")
	public UserEntity update(@RequestBody UserEntity us){
		UserEntity oldus = userMapper.selectById(us.getId());
		us.setUsername(oldus.getUsername()+"modify");
		userMapper.updateById(us);
		return us;
	}
	
	@RequestMapping("/user/{id}")
	public UserEntity findById(@PathVariable Long id){
		UserEntity us = userMapper.selectById(id);
		return us;
	}
}

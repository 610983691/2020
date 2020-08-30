package com.coulee.comsumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.comsumer.dao.UserMapper;
import com.coulee.comsumer.entity.UserEntity;
import com.coulee.comsumer.service.UserService;
import com.coulee.comsumer.vo.UserListVO;

@RestController
public class UserController {

	@Autowired
	UserMapper userMapper;
	
	@RequestMapping("/user/list")
	public List<UserEntity> findUser(){
		List<UserEntity> users = userMapper.selectList(null);
		return users;
	}
}

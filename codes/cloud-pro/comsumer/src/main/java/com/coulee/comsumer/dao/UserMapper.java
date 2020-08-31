package com.coulee.comsumer.dao;

import java.io.Serializable;
import java.util.Collection;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.coulee.comsumer.entity.UserEntity;

public interface UserMapper extends BaseMapper<UserEntity> {

	/**
	 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值
	 */
	@Cacheable(value = "pdt:product_test_consumer:t_user:id", key = "#p0")
	UserEntity selectById(Serializable id);

	/**
	 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值 新增的时候不更新缓存，只有当系统真正查询的时候才添加到缓存。
	 */
	int insert(UserEntity user);

	/**
	 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值 更新的时候，会只剔除满足这个key:id的缓存记录
	 */
	@CacheEvict(value = "pdt:product_test_consumer:t_user:id", key = "#p0.id")
	int updateById(@Param(Constants.ENTITY) UserEntity entity);

	/**
	 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值
	 * 更新的时候，剔除pdt:product_test_consumer:t_user:id下面所有的缓存
	 */
	@CacheEvict(value = "pdt:product_test_consumer:t_user:id", allEntries = true)
	int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
}

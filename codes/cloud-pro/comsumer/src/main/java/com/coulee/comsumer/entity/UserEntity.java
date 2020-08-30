package com.coulee.comsumer.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_user")
public class UserEntity {
	@TableId
	private Long  id;
	private String uid;
	private String  username;
	private Long  orgId;
}

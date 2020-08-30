package com.coulee.comsumer.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_org")
public class OrgEntity {
	@TableId
	private Long  id;
	private String  orgName;
}

package com.coulee.comsumer.redis;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String uname;
	
	private String uid;
	
	private LocalDateTime now;
	
	UserInfo (){
		uname="uname:"+System.currentTimeMillis();
		uid="uid:"+System.currentTimeMillis();
		now = LocalDateTime.now();
	}

}

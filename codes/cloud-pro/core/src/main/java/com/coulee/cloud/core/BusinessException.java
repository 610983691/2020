package com.coulee.cloud.core;

public class BusinessException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int code;
	
	public BusinessException(String msg){
		super(msg);
	}
	
	public BusinessException(String msg,Throwable thrw){
		super(msg,thrw);
	}
	
	public BusinessException(int code,String msg,Throwable thrw){
		super(msg,thrw);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}

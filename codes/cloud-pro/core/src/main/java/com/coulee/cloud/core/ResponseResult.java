package com.coulee.cloud.core;

import java.util.Collection;

import com.baomidou.mybatisplus.core.metadata.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * json响应结果包装类
 * 
 * @author zitong
 * @date 2020/09/03
 */
public class ResponseResult {

	/***
	 * 成功与否
	 */
	private boolean success;
	/**
	 * 错误码，成功为0.
	 */
	private int code;
	/***
	 * 错误提示Or消息提示
	 */
	private String msg;
	/***
	 * 响应数据
	 */
	private Object data;

	/***
	 * 数据条数，默认是0（仅返回list时有效）
	 */
	private long count = 0;

	public static ResponseResult ofSuccess(Object data) {
		if (data instanceof PageList) {
			PageList<?> pageData = (PageList<?>) data;
			return new ResponseResult().setSuccess(true).setMsg("成功").setData(pageData.getRecords()).setCount(pageData.getTotal());
		} else if (data instanceof Collection) {
			Collection<?> collectionData = (Collection<?>) data;
			return new ResponseResult().setSuccess(true).setMsg("成功").setData(data).setCount(collectionData.size());
		} else {
			return new ResponseResult().setSuccess(true).setMsg("成功").setData(data);
		}

	}

	
//	public static  ResponseResult<List<Object>> ofSuccessList(Object data) {
//		if (data instanceof Page) {
//			Page<?> pageData = (Page<?>) data;
//			return new ResponseResult<List<Object>>().setSuccess(true).setMsg("成功").setData(pageData.getRecords()).setCount(pageData.getTotal());
//		} else if (data instanceof Collection) {
//			Collection<?> collectionData = (Collection<?>) data;
//			return new ResponseResult<List<Object>>().setSuccess(true).setMsg("成功").setData(data).setCount(collectionData.size());
//		} else {
//			return new ResponseResult().setSuccess(true).setMsg("成功").setData(data);
//		}
//
//	}
	public static  ResponseResult ofSuccessMsg(String msg) {
		return new ResponseResult().setSuccess(true).setMsg(msg);
	}

	public static  ResponseResult ofFail(int code, String msg) {
		ResponseResult result = new ResponseResult();
		result.setSuccess(false);
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

	public static  ResponseResult ofThrowable(int code, Throwable throwable) {
		ResponseResult result = new ResponseResult();
		result.setSuccess(false);
		result.setCode(code);
		result.setMsg(throwable.getClass().getName() + ", " + throwable.getMessage());
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

	public ResponseResult setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public int getCode() {
		return code;
	}

	public ResponseResult setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public ResponseResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return data;
	}

	public  ResponseResult setData(Object data) {
		this.data = data;
		return this;
	}
	
	

	public ResponseResult setCount(long count) {
		this.count = count;
		return this;
	}
	
	public long getCount() {
		return this.count;
	}

	@Override
	public String toString() {
		return "Result{" + "success=" + success + ", code=" + code + ", count=" + count + ", msg='" + msg + '\''
				+ ", data=" + data + '}';
	}
}
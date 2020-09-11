package com.coulee.cloud.common.intercept;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.coulee.cloud.common.base.ResponseResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于统一的异常拦截
 * @author tongjie
 *
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionHandlerAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseResult<String> handleException(Exception e) {
		log.error("未知错误", e);
		return ResponseResult.ofFail(-1, "服务不可用");
	}

//	@ExceptionHandler(BusinessException.class)
//	public Message handleBusinessException(BusinessException e) {
//		log.info(e.getBusinessDetail(), e);
//		Message msg = new Message();
//		msg.setCode(e.getCode());
//		msg.setMsg(e.getMessage());
//		return msg;
//	}
//
//	@ExceptionHandler(HttpMessageConversionException.class)
//	public Message handleHttpMessageConversionException(HttpMessageConversionException e) {
//		log.info(e.getMessage(), e);
//		Message msg = new Message();
//		msg.setCode(HttpStatus.BAD_REQUEST.value());
//		msg.setMsg("请求参数格式错误！");
//		return msg;
//	}
//
//	@ExceptionHandler(DataAccessException.class)
//	public Message handleDataAccessException(DataAccessException e) {
//		log.warn("数据库处理异常", e);
//		Message msg = new Message();
//		msg.setCode(ExceptionCode.DATABASE_ERROR.getCode());
//		msg.setMsg(ExceptionCode.DATABASE_ERROR.getErrorMsg());
//		return msg;
//	}
//
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public Message handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//		log.info("handleMethodArgumentNotValidException异常", e);
//		List<ObjectError> errors = e.getBindingResult().getAllErrors();
//		StringBuffer errorMsg = new StringBuffer();
//		for (int i = 0; i < errors.size(); i++) {
//			if (i > 0) {
//				errorMsg.append(";");
//			}
//			errorMsg.append(errors.get(i).getDefaultMessage());
//		}
//		Message msg = new Message();
//		msg.setCode(HttpStatus.BAD_REQUEST.value());
//		msg.setMsg(errorMsg.toString());
//		return msg;
//	}
//
//	@ExceptionHandler(ConstraintViolationException.class)
//	public Message handleConstraintViolationException(ConstraintViolationException e) {
//		log.info("handleConstraintViolationException异常", e);
//		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//		int i = 0;
//		StringBuffer errorMsg = new StringBuffer();
//		for (ConstraintViolation<?> item : violations) {
//			if (i > 0) {
//				errorMsg.append(";");
//			}
//			errorMsg.append(item.getMessage());
//			i++;
//		}
//		Message msg = new Message();
//		msg.setCode(HttpStatus.BAD_REQUEST.value());
//		msg.setMsg(errorMsg.toString());
//		return msg;
//	}
}
package com.coulee.cloud.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/***
 * spring 相关配置，这里目前配置了fastjson
 * 
 * @author tongjie
 *
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    // 使用fastjson格式化输出responsebody
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 自定义配置...
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat);
        converter.setFastJsonConfig(config);
        converters.add(0, converter);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	
	}
}
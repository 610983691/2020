###sentinel

[https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D](https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D "文档")

[https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8](https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8 "使用")

[https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel](https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel "springcloud alibab")

[https://github.com/alibaba/Sentinel/wiki/%E7%BD%91%E5%85%B3%E9%99%90%E6%B5%81#spring-cloud-gateway](https://github.com/alibaba/Sentinel/wiki/%E7%BD%91%E5%85%B3%E9%99%90%E6%B5%81#spring-cloud-gateway "网关限流")

####使用
#####sentinel控制台
1.下载控制台jar包

2.执行
	
	java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar

3.登录查看控制台：
	http://47.107.142.148:8080/#/dashboard



#####sentinel 整合spring cloud gateway.


1. gateway 网关接入：

	启动参数添加：
		
		#表明是一个网关	
		-Dcsp.sentinel.app.type=1		
		-Dcsp.sentinel.dashboard.server=localhost:8080


2. 普通的client 接入：
		
		#表示注册到localhost:8080 的sentinel控制台上。
		-Dcsp.sentinel.dashboard.server=localhost:8080

3. 通常，在目前的使用场景下，我只需要把网关接入到sentinel中就行


#####配置
对于流量进行限流配置:

![sentinelconfig](doc_pic/sentinelconfig.png "sentinel限流配置")	

不断的刷新，可以看到1秒只能有1个正常的响应，其他请求都被限流。

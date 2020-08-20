##sentinel 熔断实践
###限流
个人理解，限流又分为下面几种场景：
1.通过本地接口http:url/直接访问consumer的本地local接口。
2.通过网关调用 consumer的本地local接口。
3.通过网关调用 consumer的remoteecho接口，但remoteecho接口只会被限流，一般不会抛异常。
4.通过网关调用 consumer的remoterandom接口，该接口有可能抛出一些业务异常。
####一、consumer消费者的本地local接口限流

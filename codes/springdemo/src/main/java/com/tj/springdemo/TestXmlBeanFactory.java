package com.tj.springdemo;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.context.WebApplicationContext;

public class TestXmlBeanFactory {
    public static void main(String[] args) {
        //虽然该实现已被弃用，可以看出，ioc容器，仍然可以通过该方式来构建bean
        //这里的location必须是classpath下的，这里就不深究绝对路径了，能找到这个文件把bean加载起来就行。
        BeanFactory iocContainer = new XmlBeanFactory(new DefaultResourceLoader().getResource("/ioctest.xml"));
        //加载x'm'l文件的时候，输出：Loaded 2 bean definitions from class path resource [ioctest.xml]
        //ioc容器内部对bean的定义，就是一个个的beandefinition
        System.out.println(iocContainer.getBean("beanb"));
        //beanc 的构造方法打印输出会再beanb获得的时候输出
        System.out.println(iocContainer.getBean("beanc"));
        TestXmlBeanFactory.class.getDeclaredMethods()
    }


}

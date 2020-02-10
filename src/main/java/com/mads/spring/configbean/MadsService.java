package com.mads.spring.configbean;

import com.mads.registry.BaseRegistryDelegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/*******
 * 生产者端配置
 *
 * XSD自定义标签的 madsservice 属性封装类
 *
 * @author Mads
 */
public class MadsService implements InitializingBean,ApplicationContextAware {
    private String id;
    private String intf;//暴露的接口的全路径 interface='com.aa.xxx.AAService'
    private String ref;//（可选参数）

    private static ApplicationContext application;

    /*
     * 这个方法是在MadsService类实例化以后，并且里面的属性IOC注入进来要后调用的
     *
     * MadsServiceBeanDefinitionParse中解释说每解析一个service标签就会执行一次parse方法，而Spring在
     * 实例化MadsServiceBeanDefinitionParse类时发现依赖了当前类。所以当前类也会被实例化调用。也就是说每解析一个service标签
     * 就会调用委托来注册服务一次，
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Service afterPropertiesSet " + intf + " : " + ref);

        //委托机制，帮我们进行服务注册
        BaseRegistryDelegate.registry(id,application);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.application = applicationContext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntf() {
        return intf;
    }

    public void setIntf(String intf) {
        this.intf = intf;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public static ApplicationContext getApplication() {
        return application;
    }

    public void setApplication(ApplicationContext application) {
        this.application = application;
    }
}

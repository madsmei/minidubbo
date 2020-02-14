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

    //一个接口有多个实现类，分别为每个实现类起一个名字，对应的MadsReference中也会配置这个属性来区分使用哪个实现类
    private String group;
    //给服务设置版本号，场景：消费者和生产者都是4台，现在把其中两台生产者和两台消费者升级为2.0版本，
    // 那么就会剩下的两台1.0的只会去调用1.0版本的接口，如果说2.0版本没问题就可以吧剩下的两台也升级到2.0，否则就回退版本
    private String version;
    //延迟暴露 ，Spring初始化完成以后在暴露服务，单位是 ：毫秒，避免Spring的加载死锁问题
    private String delay;
    //服务端 接受的并发控制，比如设置为10就代表并发上线是10个处理，可以加载服务上 也可以加载方法上
    private String executes;
    //客户端 发起的并发执行控制，如果<dubbo:service>和<dubbo:reference>都设置了此参数。以reference为准
    private String actives;
    //限制客户端使用的链接的最大数量，
    private String connections;
    //通过令牌验证在注册中心，来决定是否下发令牌给消费者，可以防止消费者绕过注册中心访问生产者，
    //<dubbo:service token='true'>：随即token生成，UUID算法生成
    private String token;

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

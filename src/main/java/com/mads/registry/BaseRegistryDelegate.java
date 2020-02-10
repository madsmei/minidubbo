package com.mads.registry;

import com.mads.spring.configbean.MadsRegistry;
import org.springframework.context.ApplicationContext;

import java.util.List;

/******
 * 委托者 ，这个类就是把  service配置的信息注册到注册中心
 * 具体注册中心使用Redis、zookeeper、MongoDB 就这这个类来实现
 *
 * 这样写法的好处 是将业务代码隔离开。方便后面切换实际的业务逻辑。。
 * @author
 */
public class BaseRegistryDelegate {

    /*******
     * 服务的注册
     * @param id
     * @param context
     * @return
     */
    public  static  boolean registry(String id, ApplicationContext context) {
        //通过上下文获取 注册类的配置信息
        MadsRegistry registry = context.getBean(MadsRegistry.class);
        //通过配置文件 获取 具体的注册中心实例
        BaseRegistry baseRegistry = registry.getRegistrys().get(registry.getProtocol());

        //实际注册流程
        return baseRegistry.registry(id, context);
    }

    /*****
     * 服务的列表
     * @param id
     * @param context
     * @return
     */
    public static List<String> getRegistry(String id, ApplicationContext context) {

        MadsRegistry registry = context.getBean(MadsRegistry.class);

        BaseRegistry baseRegistry = registry.getRegistrys().get(registry.getProtocol());

        return baseRegistry.getRegistry(id, context);
    }
}

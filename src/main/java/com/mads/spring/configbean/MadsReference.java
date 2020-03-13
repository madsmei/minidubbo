package com.mads.spring.configbean;

import com.mads.cluster.impl.FailfastCluster;
import com.mads.cluster.impl.FailoverCluster;
import com.mads.cluster.impl.FailsafeCluster;
import com.mads.cluster.MadsCluster;
import com.mads.enum1.MadsRPCEnum;
import com.mads.invoke.advice.MadsInvokeInvocationHandler;
import com.mads.invoke.impl.HttpInvoke;
import com.mads.invoke.MadsInvoke;
import com.mads.invoke.impl.NettyInvoke;
import com.mads.invoke.impl.RmiInvoke;
import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.impl.ConstantHashBanave;
import com.mads.loadbalance.impl.LeastActiveBanace;
import com.mads.loadbalance.impl.RandomLoadBanace;
import com.mads.loadbalance.impl.RoundLoadBanace;
import com.mads.registry.BaseRegistryDelegate;
import com.mads.watch.BaseWatchDelegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******
 * 消费者端配置
 *
 * XSD自定义标签的 madsreference 属性封装类
 * 引用配置，用于创建一个远程服务代理，一个引用可以指向多个注册中心
 *
 *
 * 会将 <dubbo:reference> 标签解析为 ReferenceBean，ReferenceBean
 * 实现了 FactoryBean，因此当它在代码中有引用时，会调用 ReferenceBean#getObject() 方法进入节点注册和服务发现流程。
 * @author
 */
public class MadsReference implements FactoryBean,ApplicationContextAware,InitializingBean {
    private String id;
    private String intf;//远程接口全路径 (interface)
    private String protocol;//协议，如果不设置 则选用全局协议
    private String loadbalance;//负载均衡的配置
    private String cluster;//集群容错协议
    private String retries;//重连次数
    private String check;//是否检查 此服务已经注册到注册中心，如果没有 消费者启动失败

    //强制调用自己本地的接口服务，一般是远程和本地都有相同的服务比如都有UserService时。
    private String injvm;
    //生产者端 通过一个接口有多个实现类，用来告诉框架我们该使用哪个实现类，对应的MadsService中也会有这个属性
    private String group;
    //给服务设置版本号，场景：消费者和生产者都是4台，现在把其中两台生产者和两台消费者升级为2.0版本，
    // 那么就会剩下的两台1.0的只会去调用1.0版本的接口，如果说2.0版本没问题就可以吧剩下的两台也升级到2.0，否则就回退版本
    //生产者配置了version参数消费者一定要配置，不然消费者掉不到任何信息
    private String version;
    //延迟暴露 ，Spring初始化完成以后在暴露服务，单位是 ：毫秒，避免Spring的加载死锁问题，
    private String deplay;
    //客户端 发起的并发执行控制，如果<dubbo:service>和<dubbo:reference>都设置了此参数。以reference为准
    private String actives;
    //限制客户端使用的链接的最大数量，
    private String connections;
    
    private MadsInvoke invoke;
    
    private ApplicationContext context;

    //RPC調用的方式列表。可以根据配置文件来选择
    private static Map<String,MadsInvoke> invokeMaps = new HashMap<String,MadsInvoke>();
    //RPC调用的 支持的负载均衡的算法
    private static Map<String, LoadBanaceBase> loadBanaceMaps = new HashMap<String,LoadBanaceBase>();
    //集群容错的支持的配置
    private static Map<String, MadsCluster> madsClusterServersMap = new HashMap<String,MadsCluster>();

    static {
        invokeMaps.put(MadsRPCEnum.HTTP.getValue(), new HttpInvoke());
        invokeMaps.put(MadsRPCEnum.RMI.getValue(), new RmiInvoke());
        invokeMaps.put(MadsRPCEnum.NETTY.getValue(), new NettyInvoke());

        loadBanaceMaps.put("random", new RandomLoadBanace());
        loadBanaceMaps.put("leastactive", new LeastActiveBanace());
        loadBanaceMaps.put("round", new RoundLoadBanace());
        loadBanaceMaps.put("constanthash", new ConstantHashBanave());

        madsClusterServersMap.put("failover", new FailoverCluster());
        madsClusterServersMap.put("failfast", new FailfastCluster());
        madsClusterServersMap.put("failsafe", new FailsafeCluster());
    }

    /**
     * @Fields registryInfo 本地缓存注册中心中的服务列表信息
     * @TODO 这里现在只是启动的时候去拉取一次注册服务列表，
     * @TODO 还差一个 数据的更新策略。可以使用redis的发布订阅渠道。ZK的观察者来来实现。。
     */
    private List<String> registryInfo = new ArrayList<String>();

    /*
     * 这个方法是在MadsReference类实例化以后，并且里面的属性IOC注入进来要后调用的
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        registryInfo = BaseRegistryDelegate.getRegistry(id, context);

        //开启 服务列表的监听刷新
        BaseWatchDelegate.startWatch(context);
    }

    /* 
     * 返回一个对象，然后被spring容器管理
     * 这个方法要返回 intf这个接口的代理实例
     */
    @Override
    public Object getObject() throws Exception {

        //有单独设置的局部RPC协议就用局部的。没有设置就启用全局的
        if(protocol != null && !"".equals(protocol)) {
            invoke = invokeMaps.get(protocol);
        } else {
            MadsProtocol protocol = context.getBean(MadsProtocol.class);
            if(protocol != null) {
                invoke = invokeMaps.get(protocol.getName());
            } else {
                invoke = invokeMaps.get("http");
            }
        }
        Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[] {Class.forName(intf)},
                new MadsInvokeInvocationHandler(invoke,this));
        
        return proxy;
    }
    
    /* 
     * 返回实例的类型
     */
    @Override
    public Class getObjectType() {
        try {
            if (intf != null && !"".equals(intf)) {
                return Class.forName(intf);
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /* 
     * 是否单例
     */
    public boolean isSingleton() {
        return true;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.context = applicationContext;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
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

    public String getCheck() {
        return check;
    }
    
    public void setCheck(String check) {
        this.check = check;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<String> getRegistryInfo() {
        return registryInfo;
    }

    public void setRegistryInfo(List<String> registryInfo) {
        this.registryInfo = registryInfo;
    }

    public static Map<String, LoadBanaceBase> getLoadBanaceMaps() {
        return loadBanaceMaps;
    }

    public static void setLoadBanaceMaps(Map<String, LoadBanaceBase> loadBanaceMaps) {
        MadsReference.loadBanaceMaps = loadBanaceMaps;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public static Map<String, MadsCluster> getMadsClusterServersMap() {
        return madsClusterServersMap;
    }

    public static void setMadsClusterServersMap(Map<String, MadsCluster> madsClusterServersMap) {
        MadsReference.madsClusterServersMap = madsClusterServersMap;
    }
}

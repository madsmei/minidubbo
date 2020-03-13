package com.mads.registry.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.redis.RedisApi;
import com.mads.registry.BaseRegistry;
import com.mads.spring.configbean.MadsProtocol;
import com.mads.spring.configbean.MadsReference;
import com.mads.spring.configbean.MadsRegistry;
import com.mads.spring.configbean.MadsService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*****
 * zookeeper注册中心
 *
 * zk的目录结构
 *      ./registrys
 *         ./serviceName
 *            ./urls
 *
 * @author mads
 */
public class ZookeeperRegistry implements BaseRegistry {

    //zk注册的根目录
    private String SERVICE_EPATH = "/registrys";


    @Override
    public boolean registry(String ref, ApplicationContext context) {
        try {
            //上下文 获取全局的配置对象
            MadsProtocol protocol = context.getBean(MadsProtocol.class);
            //服务注册配置类
            MadsRegistry registry = context.getBean(MadsRegistry.class);

            //链接 ZK
            zkStart(registry.getAddress());

            //MadsServiceBeanDefinitionParse里已经说明多，有多少个<dobbo:service/>标签就有多少个MadsService实例，
            //所以这里要把所有的service实例全部拿到，在从所有的中找到自己的实例进行注册。其他的我们不用管
            Map<String, MadsService> services = context.getBeansOfType(MadsService.class);
            if(null == services) {
                return  false;
            }
            //轮训去注册服务到ZK
            services.forEach((key,service)->{
                zkRegister(service.getId(),service.getRef());
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("registry is false");
        }
        return false;
    }

    private CuratorFramework curatorFramework;
    /****
     *  链接 ZK
     * @param zkAdredd  链接 zk的地址
     */
    public void zkStart(String zkAdredd) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkAdredd)
                .sessionTimeoutMs(40000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        curatorFramework.start();

        System.out.println("zk链接成功");
    }

    public static void main(String[] args) throws IOException {
        ZookeeperRegistry aa = new ZookeeperRegistry();
        aa.zkStart("127.0.0.1");

        aa.zkRegister("mads", "127.0.0.1:8080");

        System.in.read();
    }

    /*****
     * 注册 目录结构到ZK
     * @param serviceName   服务的名称
     * @param serviceAdress  服务的地址
     */
    public boolean zkRegister(String serviceName,String serviceAdress) {

        System.out.println("zk注册中心---服务注册开始  serviceName:"+serviceName+" serviceAdress:"+serviceAdress);

        //构建关于服务名称的地址
        String servicePath = SERVICE_EPATH + "/" + serviceName;

        try {
            //判断 如果目录不存在就创建,持久目录
            //        ./registrys
            //          ./serviceName
            if(null == curatorFramework.checkExists().forPath(servicePath)) {
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath, "0".getBytes());
            }

            String addressPath = servicePath + "/" + serviceAdress;
            //临时目录
            String rsNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath, "0".getBytes());

            System.out.println("zk注册中心---服务注册成功");

            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> getRegistry(String serviceName, ApplicationContext context)  {

        if(null == curatorFramework) {
            //服务注册配置类
            MadsRegistry registry = context.getBean(MadsRegistry.class);

            if(null == registry) {
                return Collections.emptyList();
            }
            zkStart(registry.getAddress());
        }

        String servicePath = SERVICE_EPATH + "/" + serviceName;

        try {
            //服务对应的服务地址列表
            List<String> serviceUrls = curatorFramework.getChildren().forPath(servicePath);

            //开启监听
            registerWatch(servicePath,context,serviceName);

            return serviceUrls;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /******
     * 利用 ZK本身提供的监功能动态改变服务地址变动的更新
     * @param servicePath
     */
    private void registerWatch(String servicePath, ApplicationContext context,String serviceName) {

        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, servicePath, true);

        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                //变动后的服务列表
                List<String> serviceUrls = curatorFramework.getChildren().forPath(servicePath);

                //通过上下文拿到所有的Reference对象，
                Map<String, MadsReference> references = context.getBeansOfType(MadsReference.class);
                if(null != references) {
                    references.forEach((key, reference) -> {

                        if (serviceName.equals(reference.getId())) {
                            reference.setRegistryInfo(serviceUrls);
                        }
                    });
                }
            }
        };

        childrenCache.getListenable().addListener(pathChildrenCacheListener);

        try {
            childrenCache.start();
        }catch (Exception e) {
            throw new RuntimeException("zk注册监听器失败，异常："+e);
        }

    }
}

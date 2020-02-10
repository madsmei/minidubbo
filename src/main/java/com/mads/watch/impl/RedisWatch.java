package com.mads.watch.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.registry.BaseRegistry;
import com.mads.registry.impl.RedisRegistry;
import com.mads.spring.configbean.MadsProtocol;
import com.mads.spring.configbean.MadsReference;
import com.mads.watch.RegistryWatchBase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis的服务列表监听者
 * @Description: 因为 redis 没有类似zookeeper本身提供的监听机制，所以我们起一个线程 来定时进行服务列表的更新任务
 * @Date 2020/2/9
 * @Version V1.0
 * @Author Mads
 **/
public class RedisWatch implements RegistryWatchBase {

    @Override
    public boolean startWatch(ApplicationContext context,BaseRegistry registry) {

        try {
            //当服务起来以后已经拉取过一次服务列表了，所以这里让它 10秒以后开始第一次刷新，
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            while (true) {

                Map<String, MadsReference> references = context.getBeansOfType(MadsReference.class);
                if(null != references) {
                    references.forEach((key,reference)->{

                        //重新拉取一次服务列表并保存到对象里
                        List<String> registryServers = registry.getRegistry(reference.getId(), context);
                        reference.setRegistryInfo(registryServers);

                    });
                }

                try {
                    //没10秒进行一次服务列表更新
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return false;
    }

}

package com.mads.watch;

import com.mads.registry.BaseRegistry;
import com.mads.enum1.MadsRegistryEnum;
import com.mads.spring.configbean.MadsRegistry;
import com.mads.watch.impl.MongoWatch;
import com.mads.watch.impl.RedisWatch;
import com.mads.watch.impl.ZookeeperWatch;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 服务注册列表的服务监听 委托类
 * @Date 2020/2/10
 * @Version V1.0
 * @Author Mads
 **/
public class BaseWatchDelegate {

    private  static Map<String,RegistryWatchBase> watchs = new HashMap<>();
    static {
        watchs.put(MadsRegistryEnum.REDIS.getValue(),new RedisWatch());
        watchs.put(MadsRegistryEnum.ZOOKEEPER.getValue(), new ZookeeperWatch());
        watchs.put(MadsRegistryEnum.MONGODB.getValue(), new MongoWatch());
    }

    /****
     * 开启 监听功能
     * @param context
     * @return
     */
    public static boolean startWatch(ApplicationContext context){
        MadsRegistry registry = context.getBean(MadsRegistry.class);
        if(null == registry) {
            System.out.println("BaseWatchDelegate start false. protocol is null");
            return false;
        }

        BaseRegistry baseRegistry = registry.getRegistrys().get(registry.getProtocol());

        return watchs.get(registry.getProtocol()).startWatch(context,baseRegistry);
    }

}

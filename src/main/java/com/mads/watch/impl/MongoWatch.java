package com.mads.watch.impl;

import com.mads.registry.BaseRegistry;
import com.mads.watch.RegistryWatchBase;
import org.springframework.context.ApplicationContext;

/**
 * Mongo 的服务列表刷新
 * @Description: TODO
 * @Date 2020/2/10
 * @Version V1.0
 * @Author Mads
 **/
public class MongoWatch implements RegistryWatchBase {

    @Override
    public boolean startWatch(ApplicationContext context, BaseRegistry registry) {
        //TODO 这里的逻辑和redis差不多。就不在造轮子了。。
        return false;
    }
}

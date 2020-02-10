package com.mads.watch.impl;

import com.mads.registry.BaseRegistry;
import com.mads.watch.RegistryWatchBase;
import org.springframework.context.ApplicationContext;

/**
 * @Description: TODO
 * @Date 2020/2/9
 * @Version V1.0
 * @Author Mads
 **/
public class ZookeeperWatch implements RegistryWatchBase {

    @Override
    public boolean startWatch(ApplicationContext context, BaseRegistry registry) {
        return false;
    }
}

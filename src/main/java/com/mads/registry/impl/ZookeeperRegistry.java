package com.mads.registry.impl;

import com.mads.registry.BaseRegistry;
import org.springframework.context.ApplicationContext;

import java.util.List;

/*****
 * zookeeper注册中心
 * @author mads
 */
public class ZookeeperRegistry implements BaseRegistry {
    @Override
    public boolean registry(String parm, ApplicationContext context) {
        //TODO 未完待续。。。
        return false;
    }

    @Override
    public List<String> getRegistry(String id, ApplicationContext context) {
        return null;
    }
}

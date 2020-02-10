package com.mads.registry.impl;

import com.mads.registry.BaseRegistry;
import org.springframework.context.ApplicationContext;

import java.util.List;

/*****
 * mongo注册中心
 * @author  mads
 */
public class MongoRegistry implements BaseRegistry {
    @Override
    public boolean registry(String parm, ApplicationContext context) {
        //TODO 未完待续。。，这里的实现逻辑其实和redis差不多。就不在造轮子了。。
        return false;
    }

    @Override
    public List<String> getRegistry(String id, ApplicationContext context) {
        return null;
    }
}

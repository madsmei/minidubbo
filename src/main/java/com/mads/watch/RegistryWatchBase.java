package com.mads.watch;

import com.mads.registry.BaseRegistry;
import org.springframework.context.ApplicationContext;

/**
 * 服务注册中心 监听者，用以监听 当注册服务列表发生变化后 能更新本地缓存的服务列表
 * @Date 2020/2/9
 * @Version V1.0
 * @Author Mads
 **/
public interface RegistryWatchBase {

    boolean startWatch(ApplicationContext context, BaseRegistry registry);
}

package com.mads.loadbalance.impl;

import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.LoadNodeInfo;

import java.util.List;

/**
 * @Description: 一致性hash策略，使用相同的请求参数总是请求到同一台机器，一台机器宕机可以根据虚拟节点分摊至其他提供者
 * @Date 2020/2/21
 * @Version V1.0
 * @Author Mads
 **/
public class ConstantHashBanave implements LoadBanaceBase {
    @Override
    public LoadNodeInfo doSelect(List<String> registryInfos) {
        //TODO 未完待续。。
        return null;
    }
}

package com.mads.loadbalance.impl;

import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.LoadNodeInfo;

import java.util.List;

/**
 * @Description: 最少活跃调用策略，解决 慢提供者接受更少的请求
 * @Date 2020/2/21
 * @Version V1.0
 * @Author Mads
 **/
public class LeastActiveBanace implements LoadBanaceBase {
    @Override
    public LoadNodeInfo doSelect(List<String> registryInfos) {
        //TODO 未完待续。。

        return null;
    }
}

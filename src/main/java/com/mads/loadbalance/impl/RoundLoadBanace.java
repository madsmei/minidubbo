package com.mads.loadbalance.impl;

import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.LoadNodeInfo;

import java.util.List;

/*****
 * 负载均衡算法--轮训
 */
public class RoundLoadBanace implements LoadBanaceBase {

    //轮训 下标
    private volatile static Integer index = 0;

    @Override
    public LoadNodeInfo doSelect(List<String> registryInfos) {

        String registryInfo = "";
        synchronized (index){
            if(index > registryInfos.size()){
                index = 0;
            }
            registryInfo = registryInfos.get(index.intValue());
            index++;
        }

        return LoadNodePrase.packageLoadNodeInfo(registryInfo);    }
}

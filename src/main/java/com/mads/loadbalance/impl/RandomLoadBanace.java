package com.mads.loadbalance.impl;

import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.LoadNodeInfo;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/*****
 * 负载均衡算法--随机
 * */
public class RandomLoadBanace implements LoadBanaceBase {
    @Override
    public LoadNodeInfo doSelect(List<String> registryInfos) {

        if(null == registryInfos || 0 == registryInfos.size()){
            return null;
        }

        String registryInfo = "";

        if(1 == registryInfos.size()){
            registryInfo = registryInfos.get(0);
        }else{
            int  random = ThreadLocalRandom.current().nextInt(registryInfos.size());

            registryInfo = registryInfos.get(random);
        }

        return LoadNodePraseUtil.packageLoadNodeInfo(registryInfo);
    }
}
package com.mads.invoke.impl;


import com.alibaba.fastjson.JSONObject;
import com.mads.enum1.MadsRPCEnum;
import com.mads.invoke.MadsInvocation;
import com.mads.invoke.MadsInvoke;
import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.monitor.MadsMonitor;
import com.mads.monitor.MadsMonitorDelegate;
import com.mads.netty.NettyUtil;
import com.mads.spring.configbean.MadsReference;

import java.util.List;

/******
 * Netty协议来实现RPC调用
 */
public class NettyInvoke implements MadsInvoke {

    @MadsMonitor
    @Override
    public String invoke(MadsInvocation invocation) throws Exception {
        MadsReference reference = invocation.getReference();

        //拿到服务列表
        List<String> serviceList = reference.getRegistryInfo();

        //负载均衡算法‘ 选一个服务去调用。
        //轮训。随即。
        LoadBanaceBase loadBanace = reference.getLoadBanaceMaps().get(reference.getLoadbalance());
        LoadNodeInfo nodeInfo = loadBanace.doSelect(serviceList);

        JSONObject sendParam = new JSONObject();
        sendParam.put("methodName", invocation.getMethod().getName());
        sendParam.put("serviceId", reference.getId());
        sendParam.put("methodParams", invocation.getObjs());
        //应为方法可能会出现 重载。所以一定要带上 方法的类型信息，
        sendParam.put("paramTypes", invocation.getMethod().getParameterTypes());

        try {
            String result = NettyUtil.sendMsg(nodeInfo.getHost(), nodeInfo.getPort(), sendParam.toJSONString());
            //调用统计
            MadsMonitorDelegate.doMonitor(nodeInfo,
                    sendParam.toJSONString(), MadsRPCEnum.NETTY.getValue());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
    }
    
}

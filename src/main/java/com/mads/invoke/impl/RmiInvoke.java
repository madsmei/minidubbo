package com.mads.invoke.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.enum1.MadsRPCEnum;
import com.mads.invoke.MadsInvocation;
import com.mads.invoke.MadsInvoke;
import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.monitor.MadsMonitor;
import com.mads.monitor.MadsMonitorDelegate;
import com.mads.rmi.RmiManager;
import com.mads.rmi.SoaRmi;
import com.mads.spring.configbean.MadsReference;

import java.rmi.RemoteException;
import java.util.List;

/******
 * RMI协议 实现RPC调用
 * @author
 */
public class RmiInvoke implements MadsInvoke {

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

        RmiManager rmiManager = new RmiManager();
        SoaRmi madssoa = rmiManager.startRmiClient(nodeInfo, "madssoa");

        try {
            String result = madssoa.invoke(sendParam.toJSONString());
            //调用统计
            MadsMonitorDelegate.doMonitor(nodeInfo,
                    sendParam.toJSONString(), MadsRPCEnum.RMI.getValue());

            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            throw e;
        }
    }
}

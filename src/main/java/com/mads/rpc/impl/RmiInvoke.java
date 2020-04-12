package com.mads.rpc.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.base.RpcRequest;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.rmi.RmiManager;
import com.mads.rmi.SoaRmi;
import com.mads.rpc.MadsInvocation;
import com.mads.rpc.MadsInvoke;

import java.rmi.RemoteException;

/******
 * RMI协议 实现RPC调用
 * @author
 */
public class RmiInvoke implements MadsInvoke {

    @Override
    public String invokeMethod(MadsInvocation invocation) throws Exception {

        LoadNodeInfo nodeInfo = invocation.getNodeInfo();
        RpcRequest rpcRequest = invocation.getRpcrequest();

        JSONObject sendParam = new JSONObject();
        sendParam.put("methodName", rpcRequest.getMethodName());
        sendParam.put("serviceId", rpcRequest.getServiceId());
        sendParam.put("methodParams", rpcRequest.getMethodParams());
        //应为方法可能会出现 重载。所以一定要带上 方法的类型信息，
        sendParam.put("paramTypes", rpcRequest.getParamTypes());

        RmiManager rmiManager = new RmiManager();
        SoaRmi madssoa = rmiManager.startRmiClient(nodeInfo, "madssoa");

        try {
            String result = madssoa.invoke(sendParam.toJSONString());

            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            throw e;
        }
    }
}

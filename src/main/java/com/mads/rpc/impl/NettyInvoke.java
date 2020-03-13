package com.mads.rpc.impl;


import com.alibaba.fastjson.JSONObject;
import com.mads.base.RpcRequest;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.netty.NettyUtil;
import com.mads.rpc.MadsInvocation;
import com.mads.rpc.MadsInvoke;

/******
 * Netty协议来实现RPC调用
 */
public class NettyInvoke implements MadsInvoke {

    @Override
    public String invoke(MadsInvocation invocation) throws Exception {
//        MadsReference reference = invocation.getReference();
//
//        //拿到服务列表
//        List<String> serviceList = reference.getRegistryInfo();
//
//        //负载均衡算法‘ 选一个服务去调用。
//        //轮训。随即。
//        LoadBanaceBase loadBanace = reference.getLoadBanaceMaps().get(reference.getLoadbalance());
//        LoadNodeInfo nodeInfo = loadBanace.doSelect(serviceList);
//
//        JSONObject sendParam = new JSONObject();
//        sendParam.put("methodName", invocation.getMethod().getName());
//        sendParam.put("serviceId", reference.getId());
//        sendParam.put("methodParams", invocation.getObjs());
//        //应为方法可能会出现 重载。所以一定要带上 方法的类型信息，
//        sendParam.put("paramTypes", invocation.getMethod().getParameterTypes());

        LoadNodeInfo nodeInfo = invocation.getNodeInfo();
        RpcRequest rpcRequest = invocation.getRpcrequest();

        JSONObject sendParam = new JSONObject();
        sendParam.put("methodName", rpcRequest.getMethodName());
        sendParam.put("serviceId", rpcRequest.getServiceId());
        sendParam.put("methodParams", rpcRequest.getMethodParams());
        //应为方法可能会出现 重载。所以一定要带上 方法的类型信息，
        sendParam.put("paramTypes", rpcRequest.getParamTypes());

        try {
            String result = NettyUtil.sendMsg(nodeInfo.getHost(), nodeInfo.getPort(), sendParam.toJSONString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
    }
    
}

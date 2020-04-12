package com.mads.rpc.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.base.RpcRequest;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.rpc.MadsInvocation;
import com.mads.rpc.MadsInvoke;
import com.mads.util.HttpRequestUtil;

/*******
 * Http协议 的方式来实现RPC调用
 *
 * 详细介绍和注意点 请看{@link com.mads.servlet.DispatcherServlet}有说明
 * @author mads
 */
public class HttpInvoke implements MadsInvoke {

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

        String url = "http://"+nodeInfo.getHost()+":"+nodeInfo.getPort()
                +nodeInfo.getContextpath();

        String result = HttpRequestUtil.sendPost(url, sendParam.toJSONString());

        return result;
    }

}

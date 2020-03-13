package com.mads.rpc;

import com.mads.base.RpcRequest;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.spring.configbean.MadsReference;

import java.lang.reflect.Method;

/******
 * RPC调用 的中间参数的分装类。其实分开写参数也是可以的。这里只是为了 好调用一点
 * @author
 */
public class MadsInvocation {

    private LoadNodeInfo nodeInfo;
    private RpcRequest rpcrequest;

    private MadsReference reference;
    private MadsInvoke invoke;

    public MadsInvoke getInvoke() {
        return invoke;
    }

    public void setInvoke(MadsInvoke invoke) {
        this.invoke = invoke;
    }

    public MadsReference getReference() {
        return reference;
    }

    public void setReference(MadsReference reference) {
        this.reference = reference;
    }

    public LoadNodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(LoadNodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public RpcRequest getRpcrequest() {
        return rpcrequest;
    }

    public void setRpcrequest(RpcRequest rpcrequest) {
        this.rpcrequest = rpcrequest;
    }
}

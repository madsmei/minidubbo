package com.mads.rpc.advice;

import com.mads.base.RpcRequest;
import com.mads.cluster.MadsCluster;
import com.mads.loadbalance.LoadBanaceBase;
import com.mads.loadbalance.LoadNodeInfo;
import com.mads.rpc.MadsInvocation;
import com.mads.rpc.MadsInvoke;
import com.mads.spring.configbean.MadsReference;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;


/*****8
 * 这个类就是一个增强类  advice
 *
 * 通过这个类 来进行远程的RPC调用，
 *
 * @author mads
 */
public class MadsInvokeInvocationHandler implements InvocationHandler{

    private MadsInvoke invoke;

    private MadsReference reference;

    public MadsInvokeInvocationHandler(MadsInvoke invoke, MadsReference reference) {
        this.invoke = invoke;
        this.reference = reference;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("----invoke到了MadsInvokeInvocationHandler----"+method.toString());

        //在这个invoke里面做一个远程的rpc调用。
        MadsInvocation invocation = new MadsInvocation();
//        invocation.setIntf(reference.getIntf());
//        invocation.setMethod(method);
//        invocation.setObjs(args);
        invocation.setReference(reference);
        //拿到服务列表
        List<String> serviceList = reference.getRegistryInfo();

        //负载均衡算法‘ 选一个服务去调用。
        //轮训。随即。
        LoadBanaceBase loadBanace = reference.getLoadBanaceMaps().get(reference.getLoadbalance());
        LoadNodeInfo nodeInfo = loadBanace.doSelect(serviceList);
        invocation.setNodeInfo(nodeInfo);

        invocation.setRpcrequest(new RpcRequest(reference.getId(),
                method.getName(), args, method.getParameterTypes()));


        //套用 集群容错，来进行 调用安全
        MadsCluster cluster = reference.getMadsClusterServersMap().get(reference.getCluster());
        String result = cluster.invoke(invocation);
        return result;
    }
}

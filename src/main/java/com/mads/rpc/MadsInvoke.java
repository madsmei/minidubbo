package com.mads.rpc;

import com.mads.base.RpcRequest;
import com.mads.loadbalance.LoadNodeInfo;

/******
 * 需要注意一下，RPC 是一种思想。并不是一种协议。如果有人问你RPC属于那一层（OSI7层），怼他，看不起他，
 *
 * RPC调用的方式顶级接口
 *  * 一个Rpc包含哪些部分？
 *  *  1.代理问题                  {@link com.mads.rpc.advice.MadsInvokeInvocationHandler}
 *  *  2.序列化问题                 我采用的是 JDK提供的序列化。成熟的架构是要使用别的方式。毕竟JDK的方式效率太低了
 *  *  3.通信问题                   我们使用了http {@link com.mads.rpc.impl.HttpInvoke},Netty {@link com.mads.rpc.impl.NettyInvoke},RMI {@link com.mads.rpc.impl.RmiInvoke}
 *  *  4.服务实例化问题             使用反射，执行远端的业务方法 {@link com.mads.util.ServerGetMethodUtil}
 */
public interface MadsInvoke {

    /*****
     *
     * @return
     * @throws Exception
     */
    String invokeMethod(MadsInvocation invocation) throws Exception;
}

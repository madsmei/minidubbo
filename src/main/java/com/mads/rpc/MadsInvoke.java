package com.mads.rpc;

import com.mads.base.RpcRequest;
import com.mads.loadbalance.LoadNodeInfo;

/******
 * RPC调用的方式顶级接口
 */
public interface MadsInvoke {

    /*****
     *
     * @return
     * @throws Exception
     */
    String invoke(MadsInvocation invocation) throws Exception;
}

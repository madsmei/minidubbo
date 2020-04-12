package com.mads.cluster.impl;

import com.mads.cluster.MadsCluster;
import com.mads.rpc.MadsInvocation;
import com.mads.rpc.MadsInvoke;

/*****
 * 失败了 什么都不干
 */
public class FailsafeCluster implements MadsCluster {
    
    public String invoke(MadsInvocation invocation) throws Exception {
        MadsInvoke invoke = invocation.getInvoke();
        try {
            return invoke.invokeMethod(invocation);
        } catch (Exception e) {
            e.printStackTrace();
            return "忽略异常";
        }
    }
    
}

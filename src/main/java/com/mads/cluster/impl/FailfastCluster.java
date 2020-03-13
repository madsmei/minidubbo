package com.mads.cluster.impl;

import com.mads.cluster.MadsCluster;
import com.mads.rpc.MadsInvocation;
import com.mads.rpc.MadsInvoke;

/*****
 * 失败就抛出异常
 */
public class FailfastCluster implements MadsCluster {
    
    public String invoke(MadsInvocation invocation) throws Exception {
        MadsInvoke invoke = invocation.getInvoke();
        try {
            return invoke.invoke(invocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}

package com.mads.cluster;

import com.mads.invoke.MadsInvocation;
import com.mads.invoke.MadsInvoke;

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

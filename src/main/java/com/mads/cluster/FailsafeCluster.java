package com.mads.cluster;

import com.mads.invoke.MadsInvocation;
import com.mads.invoke.MadsInvoke;

/*****
 * 失败了 什么都不干
 */
public class FailsafeCluster implements MadsCluster {
    
    public String invoke(MadsInvocation invocation) throws Exception {
        MadsInvoke invoke = invocation.getInvoke();
        try {
            return invoke.invoke(invocation);
        } catch (Exception e) {
            e.printStackTrace();
            return "忽略异常";
        }
    }
    
}

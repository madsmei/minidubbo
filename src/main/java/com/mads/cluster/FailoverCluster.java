package com.mads.cluster;

import com.mads.invoke.MadsInvocation;
import com.mads.invoke.MadsInvoke;

/*****
 * 重连机制
 * @author Mads
 */
public class FailoverCluster implements MadsCluster {

    @Override
    public String invoke(MadsInvocation invocation) throws Exception {
        String retries = invocation.getReference().getRetries();
        int count = Integer.parseInt(retries);
        
        for (int i = 0; i < count; i++) {
            try {
                MadsInvoke invoke = invocation.getInvoke();
                String result = invoke.invoke(invocation);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        
       throw new RuntimeException("连接了 "  + count + "次还失败");
    }
}

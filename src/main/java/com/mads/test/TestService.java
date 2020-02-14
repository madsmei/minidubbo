package com.mads.test;

import com.mads.invoke.MadsInvocation;
import com.mads.monitor.MadsMonitor;

/**
 * @Description: TODO
 * @Date 2020/2/14
 * @Version V1.0
 * @Author Mads
 **/
public interface TestService {

    @MadsMonitor
    void getAop(String name,MadsInvocation invocation);

}

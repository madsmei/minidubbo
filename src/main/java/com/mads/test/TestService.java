package com.mads.test;

import com.mads.rpc.MadsInvocation;

/**
 * @Description: TODO
 * @Date 2020/2/14
 * @Version V1.0
 * @Author Mads
 **/
public interface TestService {

    void getAop(String name,MadsInvocation invocation);

}

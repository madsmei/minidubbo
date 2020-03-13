package com.mads.test;

import com.mads.rpc.MadsInvocation;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Date 2020/2/14
 * @Version V1.0
 * @Author Mads
 **/
@Service
public class TestServiceImpl implements TestService {

    @Override
    public void getAop(String name,MadsInvocation invocation) {
        System.out.println("TestServiceImpl ----test aop");
    }
}

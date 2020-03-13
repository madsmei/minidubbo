package com.mads.test;

import com.mads.rpc.MadsInvocation;
import com.mads.spring.configbean.MadsReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Date 2020/2/14
 * @Version V1.0
 * @Author Mads
 **/
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/aop")
    public String testAop() {
        MadsInvocation invocation = new MadsInvocation();

        MadsReference reference = new MadsReference();
        reference.setCluster("aaaa");
        reference.setIntf("bbbb");
        reference.setProtocol("cccc");

        invocation.setReference(reference);

        testService.getAop("mads",invocation);
        return "ok";
    }

}

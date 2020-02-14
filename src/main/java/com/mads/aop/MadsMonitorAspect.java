package com.mads.aop;

import com.mads.invoke.MadsInvocation;
import com.mads.monitor.MadsMonitor;
import com.mads.monitor.MadsMonitorDelegate;
import com.mads.spring.configbean.MadsReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用过aop来开启 方法的统计工作
 * @Date 2020/2/13
 * @Version V1.0
 * @Author Mads
 **/
@Aspect
@Component
public class MadsMonitorAspect {


    /*****
     * 环绕通知。这样 所有使用了 MadsMonitor注解 都会被拦截
     * @param joinPoint
     * @return
     */
    @Around("@annotation(madsMonitor)")
    public Object around(ProceedingJoinPoint joinPoint, MadsMonitor madsMonitor) {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        System.out.println("代理类名 "+className+" 的方法名-->"+methodName);
        try {
            Map<String,Object> map = AOPGetFieldsUtil.getFieldsName(joinPoint);
            MadsInvocation invocation = (MadsInvocation) map.get("invocation");

            //-----在这里也已加一些其他业务逻辑------

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //Spring的计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //执行业务代码
        try {
            Object proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            stopWatch.stop();
            System.out.println("RPC-->"+methodName+"()方法执行时间："+stopWatch.prettyPrint());
        }
        return null;
    }


}

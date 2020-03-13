package com.mads.monitor;

import com.mads.aop.AOPGetFieldsUtil;
import com.mads.rpc.MadsInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

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


    @Pointcut("execution(* com.mads.rpc.*.*(..))")
    private void pointCutMethod() {
    }

    /*****
     * 环绕通知。这样 所有使用了 MadsMonitor注解 都会被拦截
     * @param joinPoint
     * @return
     */
    @Around("pointCutMethod()")
    public Object around(ProceedingJoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();//方法参数

        System.out.println("代理类名 "+className+" 的方法名-->"+methodName);
        try {
            Map<String,Object> map = AOPGetFieldsUtil.getFieldsName(joinPoint);
            MadsInvocation invocation = (MadsInvocation) map.get("invocation");

            //-----在这里也已加一些其他的例如保存数据库的操作------
            System.out.println("方法名："+methodName+" url:"+invocation.getNodeInfo().getUrl());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //Spring的计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            //执行业务代码
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

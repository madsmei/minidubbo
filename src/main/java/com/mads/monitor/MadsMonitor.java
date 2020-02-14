package com.mads.monitor;

import com.mads.invoke.MadsInvocation;

import java.lang.annotation.*;

/**
 * @Description: 定义一个注解来开启使用 统计功能
 * @Date 2020/2/13
 * @Version V1.0
 * @Author Mads
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MadsMonitor {

}

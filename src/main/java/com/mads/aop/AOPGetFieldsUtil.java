package com.mads.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 使用AOP时 获取得到的方法参数，包括对象类型，
 *
 * @Date 2020/2/14
 * @Version V1.0
 * @Author Mads
 **/
public class AOPGetFieldsUtil {
    private static HashMap<String, Class> map = new HashMap<String, Class>() {
        {
            put("java.lang.Integer", int.class);
            put("java.lang.Double", double.class);
            put("java.lang.Float", float.class);
            put("java.lang.Long", long.class);
            put("java.lang.Short", short.class);
            put("java.lang.Boolean", boolean.class);
            put("java.lang.Char", char.class);
        }
    };

    /*****
     * 返回的map里的key就是 使用AOP的方法参数的名字。所以这里要注意，不要乱改参数名字否则会造成AOP逻辑不能正常调用
     * @param joinPoint
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    public static Map getFieldsName(ProceedingJoinPoint joinPoint) throws ClassNotFoundException, NoSuchMethodException {
        String classType = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        // 参数值
        Object[] args = joinPoint.getArgs();
        Class<?>[] classes = new Class[args.length];
        for (int k = 0; k < args.length; k++) {
            if (!args[k].getClass().isPrimitive()) {
                // 获取的是封装类型而不是基础类型
                String result = args[k].getClass().getName();
                Class s = map.get(result);
                classes[k] = s == null ? args[k].getClass() : s;
            }
        }
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        // 获取指定的方法，第二个参数可以不传，但是为了防止有重载的现象，还是需要传入参数的类型
        Method method = Class.forName(classType).getMethod(methodName, classes);
        // 参数名
        String[] parameterNames = pnd.getParameterNames(method);
        // 通过map封装参数和参数值
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        for (int i = 0; i < parameterNames.length; i++) {
            System.out.println(parameterNames[i]+" -->"+args[i]);
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
    }
}

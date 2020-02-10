package com.mads.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mads.spring.configbean.MadsService;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Date 2020/2/9
 * @Version V1.0
 * @Author Mads
 **/
public class ServerGetMethodUtil {

    /*****
     * 通客户端的请求中拿到 各种信息 然后利用反射 调用目标方法
     * @param httpProcess
     * @return
     */
    public static String invokeMethodFromRequest(JSONObject httpProcess) {
        String serviceId = httpProcess.getString("serviceId");
        String methodName = httpProcess.getString("methodName");
        JSONArray paramTypes = httpProcess.getJSONArray("paramTypes");
        JSONArray methodParam = httpProcess.getJSONArray("methodParams");

        Object[] objs = null;
        if (methodParam != null) {
            objs = new Object[methodParam.size()];
            int i = 0;
            for (Object o : methodParam) {
                objs[i++] = o;
            }
        }

        //从spring容器中拿到serviceid对应的bean的实例吧，然后调用methodName
        ApplicationContext application = MadsService.getApplication();
        Object bean = application.getBean(serviceId);

        //反射调用方法
        Method method = getMethod(bean, methodName, paramTypes);
        try {
            if (method != null) {
                Object result = method.invoke(bean, objs);
                return result.toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "no such method";
    }


    /******
     * 用来存放 调用方法的，因为方法存在重载，所里这里我们先把所有方法名一样的全部拿出来，判断retMethod的大小大于1
     * 则说明有重载方法。然后按照 参数类型再次判断 找到我们需要的方法
     *      1.按照参数类型排重时，先判断 参数的数量是不是一样。如果不一样，则肯定不是我们需要的
     *      2.如果数量一样，就再次判断每一个的参数类型。如果参数类型不一样。则肯定不是我们需要的
     * @param bean  实例
     * @param methodName  调用的方法名
     * @param paramTypes  调用的方法参数的类型
     * @return
     */
    private static Method getMethod(Object bean, String methodName, JSONArray paramTypes) {
        Method[] methods = bean.getClass().getMethods();
        //用来存放 所有同名调用方法的
        List<Method> retMethod = new ArrayList<Method>(methods.length);

        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                retMethod.add(method);
            }
        }

        //说明没有重载的，就是我们需要的，直接返回
        if (retMethod.size() == 1) {
            return retMethod.get(0);
        }

        boolean isSameSize = false;
        boolean isSameType = false;
        mads: for (Method method : retMethod) {
            Class<?>[] types = method.getParameterTypes();

            //判断参数额数量是否一样
            if (types.length == paramTypes.size()) {
                isSameSize = true;
            }
            if (!isSameSize) {
                continue;
            }

            //数量一样时。判断参数类型是否一样
            for (int i = 0; i < types.length; i++) {
                if (types[i].toString().contains(paramTypes.getString(i))) {
                    isSameType = true;
                }else {
                    isSameType = false;
                }

                //直接继续外层循环的写法
                if (!isSameType) {
                    continue mads;
                }
            }

            if (isSameType) {
                return method;
            }
        }
        return null;
    }
}

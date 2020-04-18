package com.mads.base;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Description: 暂时没有 用到。。。。，有空余时间了，在重构下 调用逻辑
 * @Date 2020/3/13
 * @Version V1.0
 * @Author Mads
 **/
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 9203492643964740519L;

    private String serviceId;//com.aa.BB
    private String methodName;//调用的方法名
    private Object[] methodParams;//方法参数
    private Class<?>[] paramTypes;//参数的类型

    public RpcRequest(String serviceId, String methodName, Object[] methodParams, Class<?>[] paramTypes) {
        this.serviceId = serviceId;
        this.methodName = methodName;
        this.methodParams = methodParams;
        this.paramTypes = paramTypes;
    }

    public RpcRequest() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(Object[] methodParams) {
        this.methodParams = methodParams;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "serviceId='" + serviceId + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodParams=" + Arrays.toString(methodParams) +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                '}';
    }
}

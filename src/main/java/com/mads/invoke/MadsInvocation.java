package com.mads.invoke;

import com.mads.spring.configbean.MadsReference;

import java.lang.reflect.Method;

/******
 * RPC调用 的中间参数的分装类。其实分开写参数也是可以的。这里只是为了 好调用一点
 * @author
 */
public class MadsInvocation {

    private Method method;//调用的方法
    private Object[] objs;//参数
    private String intf;//intface 的值

    private MadsReference reference;

    private MadsInvoke invoke;

    public MadsInvoke getInvoke() {
        return invoke;
    }

    public void setInvoke(MadsInvoke invoke) {
        this.invoke = invoke;
    }

    public MadsReference getReference() {
        return reference;
    }

    public void setReference(MadsReference reference) {
        this.reference = reference;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getObjs() {
        return objs;
    }

    public void setObjs(Object[] objs) {
        this.objs = objs;
    }

    public String getIntf() {
        return intf;
    }

    public void setIntf(String intf) {
        this.intf = intf;
    }
}

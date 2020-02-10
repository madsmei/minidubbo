package com.mads.invoke;

/******
 * RPC调用的方式顶级接口
 */
public interface MadsInvoke {

    String invoke(MadsInvocation invocation) throws Exception;
}

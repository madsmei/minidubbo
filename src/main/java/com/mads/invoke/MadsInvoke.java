package com.mads.invoke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mads.monitor.MadsMonitor;

/******
 * RPC调用的方式顶级接口
 */
public interface MadsInvoke {

    @MadsMonitor
    String invoke(MadsInvocation invocation) throws Exception;
}

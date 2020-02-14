package com.mads.monitor;

import com.alibaba.fastjson.JSON;
import com.mads.enum1.MadsRPCEnum;
import com.mads.loadbalance.LoadNodeInfo;

/**
 * @Description:  用来 统计服务调用次数 的委托类
 *
 * 现在的写法是 直接在调用类使用，这样会吧统计的逻辑串行在正常的业务里，应该使用AOP或其他方案。
 * @Date 2020/2/10
 * @Version V1.0
 * @Author Mads
 **/
public class MadsMonitorDelegate {

    /*****
     * 统计 服务调用信息
     * @param nodeInfo
     * @param sendParam
     * @param rpc
     */
    public static void doMonitor(LoadNodeInfo nodeInfo, String sendParam, String rpc) {
        System.out.println(rpc+"->调用"+nodeInfo.getHost()+":"+nodeInfo.getPort()+"/"+nodeInfo.getContextpath()+" 参数："+sendParam);
    }
}

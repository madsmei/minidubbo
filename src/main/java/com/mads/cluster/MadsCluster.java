package com.mads.cluster;

import com.mads.rpc.MadsInvocation;

/**
 * 在生产中 各个微服务之间的调用并不能保证完全可用，比如网络波动、机房故障、等等。。。
 * dubbo里没有熔断的技术，但是有集群容错的方式，我们要根据配置 来选择不同的处理，比如 重连、
 * @Date 2020/2/9
 * @Version V1.0
 * @Author Mads
 **/
public interface MadsCluster {
    public String invoke(MadsInvocation invocation) throws Exception;
}

package com.mads.loadbalance;

import java.util.List;

/******
 * 负载均衡算法的 顶级接口
 */
public interface LoadBanaceBase {

    /******
     * 选择 一个服务进行后面的 调用
     * @param registryInfos
     * @return
     */
    LoadNodeInfo doSelect(List<String> registryInfos);


}

package com.mads.loadbalance.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.loadbalance.LoadNodeInfo;

import java.util.Collection;

/*****
 * 拼装  属性的 基础方法
 * @author  mads
 */
public class LoadNodePraseUtil {

    /******
     * 这里涉及 zk注册中心和redis注册中心，数据是不一样的，所以我们要区分开
     *  将注册信息 封装成对象
     * @param registryInfo
     * @return
     */
    public static LoadNodeInfo packageLoadNodeInfo(String registryInfo){

        if(registryInfo.contains("protocol") && registryInfo.contains("{")) {
            //redis注册中心数据格式
            return redisRegistryInfo(registryInfo);
        }
        //暂时 是 ZK注册中心数据格式
        LoadNodeInfo nodeInfo = new LoadNodeInfo();
        nodeInfo.setUrl(registryInfo);

        return nodeInfo;

    }

    private static LoadNodeInfo redisRegistryInfo(String registryInfo) {

        /****
         * 这里要结合 注册时的数据结构来解析，详情看  {@link com.mads.registry.impl.RedisRegistry}
         */
        JSONObject jsonObject = JSONObject.parseObject(registryInfo);
        Collection<Object> values = jsonObject.values();

//            JSONObject node = new JSONObject();
//
//            for (Object value:values) {
//                node = JSONObject.parseObject(value.toString());
//            }
//            JSONObject protocal = node.getJSONObject("protocal");

        JSONObject protocal = JSONObject.parseObject(values.stream().findFirst().get().toString()).getJSONObject("protocal");

        LoadNodeInfo nodeInfo = new LoadNodeInfo();
        nodeInfo.setHost(protocal.getString("host") != null ? protocal.getString("host") : "");
        nodeInfo.setPort(protocal.getString("port") != null ? protocal.getString("port") : "");
        nodeInfo.setContextpath(protocal.get("contextpath") != null ? protocal.getString("contextpath") : "");

        return nodeInfo;
    }
}

package com.mads.loadbalance.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.loadbalance.LoadNodeInfo;

import java.util.Collection;

/*****
 * 拼装  属性的 基础方法
 * @author  mads
 */
public class LoadNodePrase {

    /******
     *  将注册信息 封装成对象
     * @param registryInfo
     * @return
     */
    public static LoadNodeInfo packageLoadNodeInfo(String registryInfo){

        //这里要结合 注册时的数据结构来解析，详情看  RedisRegistry

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

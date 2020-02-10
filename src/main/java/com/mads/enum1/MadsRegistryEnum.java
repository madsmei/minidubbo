package com.mads.enum1;

/**
 * @Description: 注册中心 协议配置
 * @Date 2020/2/10
 * @Version V1.0
 * @Author Mads
 **/
public enum MadsRegistryEnum {
    REDIS("redis"),
    ZOOKEEPER("zookeeper"),
    MONGODB("mongodb");


    private String value;

    private MadsRegistryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

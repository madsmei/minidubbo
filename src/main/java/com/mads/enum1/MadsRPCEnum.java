package com.mads.enum1;

/**
 * @Description: RPC协议 协议配置
 * @Date 2020/2/10
 * @Version V1.0
 * @Author Mads
 **/
public enum MadsRPCEnum {
    HTTP("http"),
    RMI("rmi"),
    NETTY("netty");


    private String value;

    private MadsRPCEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

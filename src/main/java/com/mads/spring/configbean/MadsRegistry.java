package com.mads.spring.configbean;

import com.mads.registry.BaseRegistry;
import com.mads.enum1.MadsRegistryEnum;
import com.mads.registry.impl.MongoRegistry;
import com.mads.registry.impl.RedisRegistry;
import com.mads.registry.impl.ZookeeperRegistry;

import java.util.HashMap;
import java.util.Map;

/*******
 * 生产者配置
 *
 * XSD自定义标签的 madsregistry 属性封装类
 *
 * 注册中心配置
 *
 * @author
 */
public class MadsRegistry {
    private String id;
    private String protocol;//注册协议  redis/zookeeper/。。。
    private String address;//注册地址

    //注册中心可选列表
    private static Map<String, BaseRegistry> registrys = new HashMap<String,BaseRegistry>();
    
    static {
        registrys.put(MadsRegistryEnum.REDIS.getValue(), new RedisRegistry());
        registrys.put(MadsRegistryEnum.ZOOKEEPER.getValue(), new ZookeeperRegistry());
        registrys.put(MadsRegistryEnum.MONGODB.getValue(), new MongoRegistry());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Map<String, BaseRegistry> getRegistrys() {
        return registrys;
    }

    public static void setRegistrys(Map<String, BaseRegistry> registrys) {
        MadsRegistry.registrys = registrys;
    }
}

package com.mads.registry.impl;

import com.alibaba.fastjson.JSONObject;
import com.mads.redis.RedisApi;
import com.mads.registry.BaseRegistry;
import com.mads.spring.configbean.MadsProtocol;
import com.mads.spring.configbean.MadsRegistry;
import com.mads.spring.configbean.MadsService;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/*******
 * redis 作为注册中心
 *
 * 思路：
 *      用redis作为注册中心，就需要确认  key 和 value 应该怎样设计是关键，
 *  在微服务中，每一个接口就相当于一个服务，例如说 我们现在有一个UserService，此时将它看成是一个服务，现在就需要
 *  将它注册到redis里，理所当然的想到可以用 userService来作为key，
 *  接下来就是确定value的结构，稍微想一下 比如用户服务部署了2台服务器，ip和端口号：127.0.0.1:8081,127.0.0.2:8082
 *  那么我们肯定要将这两台都注册到redis中，那么value中就要包含 ip:prot这两个信息，一般情况下我们的数据就够用了。
 *  如果我们先对注册的服务进行监控，看它的配置信息什么的就不太够用了吧，我们就把 服务注册时的配置信息也都存着吧，
 *  redis 所有的service注册使用list来存储， 单个
*       Value的结构体
*       {host:port :
*            {
*              protocol : JSONObject.toJsonString(MadsProtocol.class),
*              service : JSONObject.toJsonString(MadsService.class)
*             }
*       }
 *  其他信息可以根据实际情况来添加
 *
 * @author mads
 */
public class RedisRegistry implements BaseRegistry {

    @Override
    public boolean registry(String ref, ApplicationContext context) {

        try {
            //上下文 获取全局的配置对象
            MadsProtocol protocol = context.getBean(MadsProtocol.class);
            //服务注册配置类
            MadsRegistry registry = context.getBean(MadsRegistry.class);
            //初始化 redis的连接池
            RedisApi.createJedisPool(registry.getAddress());

            //MadsServiceBeanDefinitionParse里已经说明多，有多少个<dobbo:service/>标签就有多少个MadsService实例，
            //所以这里要把所有的service实例全部拿到，在从所有的中找到自己的实例进行注册。其他的我们不用管
            Map<String, MadsService> services = context.getBeansOfType(MadsService.class);

            services.forEach((key,service)->{
                //这个if就对应一个service标签的注册信息
                if(service.getRef().equals(ref)) {
                    JSONObject jo = new JSONObject();
                    jo.put("protocal", JSONObject.toJSONString(protocol));
                    jo.put("service", JSONObject.toJSONString(service));

                    JSONObject hostport = new JSONObject();
                    String hostportKey = protocol.getHost()+":"+protocol.getPort();
                    hostport.put(hostportKey, jo);

                    //需要把service标签的注册信息，加入到对应的服务中
                    lpush(hostport, ref,hostportKey);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("registry is false");
        }
        return false;
    }

    private synchronized void lpush(JSONObject object, String key, String hostportKey) {
        if(RedisApi.exists(key)){
            //拉取 redis中已经注册的信息
            List<String> registryServices = RedisApi.lrange(key);

            if(null != registryServices && registryServices.size() > 0){

                AtomicBoolean havaOlad = new AtomicBoolean(false);
                //存放新的注册service信息
                List<String> services_list_new = new ArrayList<String>(registryServices.size());

                //这个循环的目的，有可能生产者启动的时候修改了某一些配置信息，这时候就需要把该生产者原来的信息替换成修改后的信息
                registryServices.stream().forEach(registryService->{
                    JSONObject service_jsonObject = JSONObject.parseObject(registryService);

                    //如果registryInfo注册信息里面包含了这个service的配置信息，说明以前这个生产者注册过
                    //这时候再启动的话，要把之前的service的注册信息，替换
                    if(service_jsonObject.containsKey(hostportKey)){
                        services_list_new.add(object.toJSONString());

                        havaOlad.set(true);
                    }else {
                        //如果没有，那就说明之前这个service标签没有注册过，这时候可能是一个新的生产者启动
                        services_list_new.add(registryService);
                    }
                });

                if (havaOlad.get()) {
                    RedisApi.del(key);

                    RedisApi.lpush(key, services_list_new.toArray(new String[services_list_new.size()]));
                }else{
                    //如果没有配置信息修改，就把新的hostport对象加入到list中
                    RedisApi.lpush(key, object.toJSONString());
                }
            }

        }else {
            //当第一次注册
            RedisApi.lpush(key, object.toJSONString());
        }
    }

    @Override
    public List<String> getRegistry(String id, ApplicationContext context) {

        //服务注册配置类
        MadsRegistry registry = context.getBean(MadsRegistry.class);
        //初始化 redis的连接池
        RedisApi.createJedisPool(registry.getAddress());

        return RedisApi.lrange(id);
    }
}

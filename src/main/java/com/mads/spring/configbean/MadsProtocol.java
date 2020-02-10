package com.mads.spring.configbean;

import com.mads.netty.NettyUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/*******
 * 生产者端配置
 *
 * XSD自定义标签的 madsprotocol 属性封装类
 * 全局 应该只有一个<dubbo:protocol 标签
 *
 * 协议配置，用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受
 * 这个类负责Netty的服务端的启动，所以实现了ApplicationListener接口，并重写onApplicationEvent方法,
 * 一定要注意使用ContextRefreshedEvent事件
 *
 * @author Mads
 *
 */
public class MadsProtocol implements ApplicationListener<ContextRefreshedEvent> {
    private String id;
    private String port;//端口
    private String name;//全局协议
    private String host;//(可选)
    private String contextpath;//工程上下文路径


    /*****
     * ContextRefreshedEvent 事件保证了 当Spring配置全部加载完以后再执行此逻辑
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //只有 启用 netty协议时 才会去启动netty
        if(!"netty".equals(name)) {
            return;
        }

        /****
         * 因为 Netty启动时会阻塞在f.channel().closeFuture().sync();为了不影响其他逻辑。我们使用线程来进行
         */
        new Thread(()->{
            try {
                //启动 Netty的服务端
                NettyUtil.startServer(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
    }


}

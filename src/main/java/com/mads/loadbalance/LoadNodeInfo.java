package com.mads.loadbalance;

/*****
 * 单个节点信息
 */
public class LoadNodeInfo {

    private String host;//远程调用目标

    private String port;//远程的端口
    //工程上下文 一般取值是  http协议下面 ：/项目名/+生产者端配置自定义Servlet时起的名字(/soa/api)详情看 DispatcherServlet
    private String contextpath;

    public LoadNodeInfo(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public LoadNodeInfo() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
    }
}

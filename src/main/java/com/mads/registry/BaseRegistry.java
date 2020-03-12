package com.mads.registry;

import org.springframework.context.ApplicationContext;
import sun.awt.AppContext;

import java.util.List;

/*****
 * 服务注册和发现
 * @author mads
 */
public interface BaseRegistry {

    /******
     * 向注册中心注册
     * @param intf
     * @param context
     * @return
     */
    boolean registry(String intf, ApplicationContext context);

    /******
     * 拉取注册服务列表
     * @param id
     * @param context
     * @return
     */
    List<String> getRegistry(String id, ApplicationContext context) ;

}

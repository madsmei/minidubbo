package com.mads.rmi;

import com.mads.loadbalance.LoadNodeInfo;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


/*****
 * RMI 协议 也很少用
 */
public class RmiManager {
    /******
     * 启动RMI的服务端
     * @param host
     * @param port
     * @param id
     */
    public void startRmiServer(String host, String port, String id) {
        try {
            SoaRmiImpl soaRmiImpl = new SoaRmiImpl();
            LocateRegistry.createRegistry(Integer.parseInt(port));
            // rmi://127.0.0.1:5689/madssoa
            Naming.bind("rmi://" + host + ":" + port + "/" + id, soaRmiImpl);

            System.out.println("server: 对象绑定成功");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }


    /*****
     * 获取一个RMI的客户端
     * @param nodeinfo
     * @param id
     * @return
     */
    public SoaRmi startRmiClient(LoadNodeInfo nodeinfo, String id) {
        String host = nodeinfo.getHost();
        String port = nodeinfo.getPort();
        try {
            return (SoaRmi)Naming.lookup("rmi://" + host + ":" + port + "/" + id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

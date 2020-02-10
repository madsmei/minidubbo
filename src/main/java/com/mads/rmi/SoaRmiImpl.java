package com.mads.rmi;

import com.alibaba.fastjson.JSONObject;
import com.mads.util.ServerGetMethodUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** 
 * @Author  Mads
 *
 * 服务端的rmi
 */

public class SoaRmiImpl extends UnicastRemoteObject implements SoaRmi {
    
    protected SoaRmiImpl() throws RemoteException {
        super();
    }
    
    public String invoke(String param) throws RemoteException {

        Object result = ServerGetMethodUtil.invokeMethodFromRequest(JSONObject.parseObject(param));
        return result.toString();
    }
    
}

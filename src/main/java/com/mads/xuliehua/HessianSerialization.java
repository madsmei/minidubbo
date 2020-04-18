package com.mads.xuliehua;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.mads.base.RpcRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description: Hessian序列化方式
 * 注意：使用hessian序列化时，一定要注意子类和父类不能有同名字段
 * @Date 2020/4/14
 * @Version V1.0
 * @Author Mads
 **/
public class HessianSerialization  {
    /**
     * Hessian实现序列化
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Object object){
        ByteArrayOutputStream byteArrayOutputStream = null;
        HessianOutput hessianOutput = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            // Hessian的序列化输出
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hessianOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Hessian实现反序列化
     * @param employeeArray
     * @return
     */
    public static Object deserialize(byte[] employeeArray) {
        ByteArrayInputStream byteArrayInputStream = null;
        HessianInput hessianInput = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(employeeArray);
            // Hessian的反序列化读取对象
            hessianInput = new HessianInput(byteArrayInputStream);
            return hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hessianInput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String [] args) {

        RpcRequest employee = new RpcRequest();
        employee.setMethodName("aaaa");
        employee.setServiceId("bbbb");

        // 序列化
        byte[] serialize = serialize(employee);
        System.out.println(serialize);
        // 反序列化
        RpcRequest deserialize = (RpcRequest)deserialize(serialize);
        System.out.println(deserialize.toString());

    }

}

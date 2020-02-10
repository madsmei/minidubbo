package com.mads.netty;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mads.spring.configbean.MadsService;
import com.mads.util.ServerGetMethodUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/******
 * Netty的 服务端的处理
 */
public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public boolean isSharable() {
        // TODO Auto-generated method stub
        return super.isSharable();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelInactive(ctx);
    }

    /*****
     * 读取 客户端请求信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf result = (ByteBuf)msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        String resultStr = new String(result1);
        System.out.println("Client said : " + resultStr);
        result.release();
        String response = invokeService(resultStr);
        
        //这个就是返回值的响应过程
        ByteBuf encode = ctx.alloc().buffer(4 * response.length());
        encode.writeBytes(response.getBytes());
        ctx.writeAndFlush(encode);
        ctx.close();
    }
    
    private String invokeService(String param) {
        String result = ServerGetMethodUtil.invokeMethodFromRequest(JSONObject.parseObject(param));
        return result.toString();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}

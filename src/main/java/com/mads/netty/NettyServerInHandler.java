package com.mads.netty;

import com.alibaba.fastjson.JSONObject;
import com.mads.base.RpcRequest;
import com.mads.util.ServerGetMethodUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/******
 * Netty的 服务端的处理
 */
public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

    /*****
     * 读取 客户端请求信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

//        RpcRequest rpcRequest = (RpcRequest)msg;

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

}

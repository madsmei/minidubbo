package com.mads.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/*****
 * Netty的客户端的处理
 */
public class NettyClientInHandler extends ChannelInboundHandlerAdapter {
    
    private Object response;//返回信息

    public Object getResponse() {
        return response;
    }

    /*****
     * 从 通道里 读取返回信息
     * @param ctx
     * @param msg  服务端的返回信息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("==============channel--channelRead================");
        response = msg;
    }
    
}

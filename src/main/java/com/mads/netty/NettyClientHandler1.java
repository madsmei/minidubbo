package com.mads.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Description: TODO
 * @Date 2020/4/13
 * @Version V1.0
 * @Author Mads
 **/
public class NettyClientHandler1 extends SimpleChannelInboundHandler<ByteBuf> {

    private Object response;//返回信息

    public Object getResponse() {
        return response;
    }
    /******
     * 客户端读取数据后，执行逻辑
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        response = byteBuf.toString();
    }

    /*****
     * 链接 建立以后
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("I am netty client",CharsetUtil.UTF_8));
    }

    /*****
     * 当网络发生异常以后的处理
     *  打印异常，并关闭 链接
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();

        ctx.close();
    }
}

package com.mads.netty;

import com.alibaba.fastjson.JSONObject;
import com.mads.base.RpcRequest;
import com.mads.util.ServerGetMethodUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/******
 * Netty的 服务端的处理
 *
 * @ChannelHandler.Sharable注解 标明 此Handler是共享的。如果不共享的话，没有一个客户端请求就要new一个Handler对象出来
 *
 */
@ChannelHandler.Sharable
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

        String msgString = result.toString(CharsetUtil.UTF_8);


//        byte[] result1 = new byte[result.readableBytes()];
//        result.readBytes(result1);
//        String resultStr = new String(result1);
        System.out.println("Client said : " + msgString);
        result.release();
        //加上系统的换行符，解决 粘包 半包问题
        String response = invokeService(msgString)+System.getProperty("line.separator");

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

    /*****
     * 读完以后的操作，比如果一个大对象在缓冲区里 1000字节。每次读500.要读2次，
     *      那么这个方法会调用两次。而上面那个channelRead（）只调用一次
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        // 写一个空。然后关闭客户端连接
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
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

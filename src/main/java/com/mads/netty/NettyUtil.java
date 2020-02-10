package com.mads.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 * @// FIXME: 2020/2/10  这里使用的 的Netty的版本比较低。只是为了演示，后面可以换成新版本来完成
 * @Description: Netty的操作工具类
 * @Date 2020/2/9
 * @Version V1.0
 * @Author Mads
 **/
public class NettyUtil {

    /*****
     *  启动 Netty的服务端服务
     * @param port
     * @throws Exception
     */
    public static void startServer(String port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new NettyServerInHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128);
            ChannelFuture f = b.bind(Integer.parseInt(port)).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /******
     * 发送消息
     * @param host
     * @param port
     * @param sendmsg
     * @return
     * @throws Exception
     */
    public static String sendMsg(String host,String port,final String sendmsg) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final StringBuffer resultmsg = new StringBuffer();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch)
                    throws Exception {
                ch.pipeline().addLast(new NettyClientInHandler(resultmsg,sendmsg));
            }
        });

        //借由ChannelFuture完成了Netty的同步请求
        ChannelFuture f = b.connect(host,Integer.parseInt(port)).channel().closeFuture().await();

        return resultmsg.toString();
    }



}

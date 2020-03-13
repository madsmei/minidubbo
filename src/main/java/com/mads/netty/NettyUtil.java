package com.mads.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

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

        try{
            ServerBootstrap b = new ServerBootstrap();//netty的启动类
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("frameDecoder",
                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
                            pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                            pipeline.addLast("encoder",new ObjectEncoder());
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            //IO数据的交互 都在这个增强类里
                            pipeline.addLast(new NettyServerInHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(Integer.parseInt(port)).sync();
            System.out.println("netty服务端启动成功，等待客户端的链接");

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /******
     * netty 客户端
     * 发送消息
     * @param host
     * @param port
     * @param sendmsg
     * @return
     * @throws Exception
     */
    public static String sendMsg(String host,String port,final String sendmsg) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        NettyClientInHandler clientInHandler = new NettyClientInHandler();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch)
                        throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("frameDecoder",
                            new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
                    pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                    pipeline.addLast("encoder",new ObjectEncoder());
                    pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                    pipeline.addLast(clientInHandler);
                }
            });

            //借由ChannelFuture完成了Netty的同步请求
            ChannelFuture future = b.connect(host,Integer.parseInt(port)).sync();
            future.channel().writeAndFlush(sendmsg);
            future.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            workerGroup.shutdownGracefully();
        }

        return (String)clientInHandler.getResponse();
    }

}

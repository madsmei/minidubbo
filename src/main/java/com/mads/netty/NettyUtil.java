package com.mads.netty;

import com.mads.xuliehua.kyro.KryoDecoder;
import com.mads.xuliehua.kyro.KryoEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;

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
            .localAddress(new InetSocketAddress(Integer.parseInt(port)))
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch)
                        throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new ObjectEncoder());
                    pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                    //超时检测
                    pipeline.addLast(new ReadTimeoutHandler(10));
                    //反序列化
                    pipeline.addLast(new KryoDecoder());
                    //序列化
                    pipeline.addLast(new KryoEncoder());
                    //给发送出去的消息增加长度字段
//                    pipeline.addLast(new LengthFieldPrepender(4));
                    //定长的方式解决 粘包半包
//                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));

                    //这两行是解决粘包，原理就是读取知道遇到 有\n或\r\n
                    pipeline.addLast(new LineBasedFrameDecoder(1024));
//                    pipeline.addLast(new StringDecoder());

                    //IO数据的交互 都在这个增强类里
                    pipeline.addLast(new NettyServerInHandler());
                }
            }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind().sync();
            System.out.println("netty服务端启动成功，等待客户端的链接");

            f.channel().closeFuture().sync();
        } finally {
            //优雅 关闭
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

        NettyClientHandler clientInHandler = new NettyClientHandler();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
            .channel(NioSocketChannel.class)
            .remoteAddress(new InetSocketAddress(host, Integer.parseInt(port)))
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch)
                        throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new ObjectEncoder());
                    //给发送出去的消息增加长度字段
//                    pipeline.addLast(new LengthFieldPrepender(4));
                    //定长的方式解决 粘包半包
//                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));

                    //这两行是解决粘包，原理就是读取知道遇到 有\n或\r\n
                    pipeline.addLast(new LineBasedFrameDecoder(1024));
//                    pipeline.addLast(new StringDecoder());

                    pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
//                    pipeline.addLast(new SerializationHandler(serialization))
//                    pipeline.addLast(new DeserializationHandler(serialization));
                    pipeline.addLast(clientInHandler);
                }
            });

            //借由ChannelFuture完成了Netty的同步请求，因为NIO的机制，他发出建立链接的事件后就立即返回了
            //此时我们还不知道 到底能不能成功建立链接，此时我们设置一个future 来拿到建立链接的通知结果
            ChannelFuture future = b.connect().sync();

            future.channel().writeAndFlush(sendmsg+"\n");

            //阻塞 直到channel 发生了关闭
            future.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            //优雅关闭
            workerGroup.shutdownGracefully();
        }

        return (String)clientInHandler.getResponse();
    }

}

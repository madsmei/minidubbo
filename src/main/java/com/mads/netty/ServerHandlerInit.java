package com.mads.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import javax.net.ssl.SSLContext;

/**
 * @Description: TODO
 * @Date 2020/4/18
 * @Version V1.0
 * @Author Mads
 **/
public class ServerHandlerInit extends ChannelInitializer<SocketChannel> {

    private final SSLContext sslContext;

    public ServerHandlerInit(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //处理Httpf服务的关键Handler
        pipeline.addLast("encoder",new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestEncoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(10*1024*1024));
        pipeline.addLast("",);
    }
}

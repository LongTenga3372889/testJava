package com.test.test.netty.demo2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;

public class DemoNetty2Client {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String host = "127.0.0.1";
        new DemoNetty2Client().connect(host,port);
    }

    private void connect(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ByteBuf spit = Unpooled.copiedBuffer("$_".getBytes(StandardCharsets.UTF_8));
                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,spit)).addLast(new StringDecoder()).addLast(new MyDemo2ClientHandler());
                }
            });
            ChannelFuture cf = bootstrap.connect(host,port).sync();
            cf.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}

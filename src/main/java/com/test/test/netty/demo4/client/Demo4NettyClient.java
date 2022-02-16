package com.test.test.netty.demo4.client;

import com.test.test.netty.demo4.decoder.MsgpackDecoderDemo4;
import com.test.test.netty.demo4.decoder.MsgpackEncoderDemo4;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class Demo4NettyClient {

    public static void main(String[] args) throws InterruptedException {
        int port = 8000;
        String host = "127.0.0.1";
        new Demo4NettyClient().connect(host,port);
    }

    private void connect(String host, int port) throws InterruptedException {
        EventLoopGroup client = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(client).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2))
                                    .addLast("msgpack decoder",new MsgpackDecoderDemo4())
                                    .addLast("frameEncoder",new LengthFieldPrepender(2))
                                    .addLast("msgpack encoder",new MsgpackEncoderDemo4())
                                    .addLast(new MyDemo4ClientHandler(1000));
                        }
                    });
            ChannelFuture cf = bootstrap.connect(host,port).sync();
            cf.channel().closeFuture().sync();
        } finally {
            client.shutdownGracefully();
        }
    }

}

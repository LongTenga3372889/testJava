package com.test.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardServer {

    private final static Logger logger = LoggerFactory.getLogger(DiscardServer.class);

    private int port;

    public DiscardServer (int port) {
        this.port = port;
    }

    public void run () throws Exception {
        EventLoopGroup eventGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        logger.info("port is :{}",port);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(eventGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
//                                    .addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
//                                    .addLast("decoder", new StringDecoder())
//                                    .addLast("encoder", new StringEncoder())
                                    .addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            eventGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new DiscardServer(8080).run();
    }

}

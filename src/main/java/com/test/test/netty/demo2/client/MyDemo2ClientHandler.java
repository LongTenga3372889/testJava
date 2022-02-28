package com.test.test.netty.demo2.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MyDemo2ClientHandler extends ChannelInboundHandlerAdapter {

    private int counter;

    private final static Logger logger = LoggerFactory.getLogger(MyDemo2ClientHandler.class);

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String message = "hello service.$_";
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes(StandardCharsets.UTF_8)));
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("this counter is {},message is {}",++counter,msg);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}

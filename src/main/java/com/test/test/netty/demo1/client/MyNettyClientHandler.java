package com.test.test.netty.demo1.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MyNettyClientHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MyNettyClientHandler.class);

    private final ByteBuf message;

    private byte[] bytes;

    private int count = 0;

    public MyNettyClientHandler() {
        bytes = ("service hello" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8);
        this.message = Unpooled.buffer(bytes.length);
        this.message.writeBytes(bytes);
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf body = null;
        for (int i = 1; i < 100; i++) {
            body = Unpooled.buffer(bytes.length);
            body.writeBytes(bytes);
            ctx.writeAndFlush(body);
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        logger.info("netty service say is {} ,count is {}",message,++count);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("MyNettyClientHandler start error", cause);
        ctx.close();
    }

}

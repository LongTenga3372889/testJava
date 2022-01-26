package com.test.test.netty.demo1.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MyNettyClientHandler extends ChannelHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MyNettyClientHandler.class);

    private final ByteBuf message;

    private byte[] bytes;

    private int count = 0;

    public MyNettyClientHandler() {
        bytes = ("service hello" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8);
        this.message = Unpooled.buffer(bytes.length);
        this.message.writeBytes(bytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf body = null;
        for (int i = 1; i < 100; i++) {
            body = Unpooled.buffer(bytes.length);
            body.writeBytes(bytes);
            ctx.writeAndFlush(body);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//        String body = new String(bytes, StandardCharsets.UTF_8);
//        logger.info("netty service say is {},count is {}", body,++count);
        String message = (String) msg;
        logger.info("netty service say is {} ,count is {}",message,++count);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("MyNettyClientHandler start error", cause);
        ctx.close();
    }

}

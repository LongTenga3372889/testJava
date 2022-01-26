package com.test.test.netty.demo1.service;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MyNettyServiceHandler extends ChannelHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MyNettyServiceHandler.class);

    private int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buffer = (ByteBuf) msg;
//        byte[] bytes = new byte[buffer.readableBytes()];
//        buffer.readBytes(bytes);
//        String message = new String(bytes, StandardCharsets.UTF_8).substring(0, bytes.length - System.getProperty("line.separator").length());
//        logger.info("read message.message is {} count is {}", message, ++count);
//        ByteBuf resp = Unpooled.copiedBuffer(("client hello" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
//        ctx.writeAndFlush(resp);
        String message = (String) msg;
        logger.info("read message is {} ,count is {}",message,++count);
        ByteBuf byteBuf = Unpooled.copiedBuffer(("client hello" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}

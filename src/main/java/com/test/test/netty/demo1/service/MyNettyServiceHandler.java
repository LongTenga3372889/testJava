package com.test.test.netty.demo1.service;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MyNettyServiceHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MyNettyServiceHandler.class);

    private int count = 0;

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        logger.info("read message is {} ,count is {}",message,++count);
        ByteBuf byteBuf = Unpooled.copiedBuffer(("client hello" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}

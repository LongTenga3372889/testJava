package com.test.test.netty.demo2.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class MyNettyDemo2Service extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MyNettyDemo2Service.class);

    private int count = 0;

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        logger.info("client send count is {}, time is {} ,message is {}", ++count, new Date().getTime(), message);
        ByteBuf byteBuf = Unpooled.copiedBuffer(("hello client.$_").getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}

package com.test.test.netty.demo3.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyNetty3ServiceHandle extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MyNetty3ServiceHandle.class);

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("service consignment message is {}",msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}

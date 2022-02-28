package com.test.test.netty.demo4.service;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyNetty4ServiceHandle extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MyNetty4ServiceHandle.class);

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("service consignment message is {}",msg);
//        ctx.write(msg);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

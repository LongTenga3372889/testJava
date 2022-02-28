package com.test.test.netty.demo4.client;

import com.test.test.netty.dto.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDemo4ClientHandler extends ChannelInboundHandlerAdapter {

    private int sendCount;

    private final static Logger logger = LoggerFactory.getLogger(MyDemo4ClientHandler.class);

    public MyDemo4ClientHandler(int number) {
        this.sendCount = number;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] userInfos = UserInfo.userInfos(sendCount);
        for (UserInfo userInfo:userInfos) {
            ctx.writeAndFlush(userInfo);
        }
//        ctx.flush();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("client read message is {}",msg);
//        ctx.write(msg);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}

package com.test.test.netty.demo4.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class MsgpackEncoderDemo4 extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, ByteBuf byteBuf) throws Exception {
        MessagePack messagePack = new MessagePack();
        byte[] message = messagePack.write(object);
        byteBuf.writeBytes(message);
    }
}

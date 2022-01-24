package com.test.test.asynAIO.service;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AIOAsynServiceHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AIOAsynServiceHandler attachment) {
        attachment.getAsynchronousServerSocketChannel().accept(attachment,this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer,byteBuffer,new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AIOAsynServiceHandler attachment) {
        exc.printStackTrace();
        attachment.getLatch().countDown();
    }
}

package com.test.test.asynAIO.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class AIOAsynClientHandler implements Runnable, CompletionHandler<Void,AIOAsynClientHandler> {

    private final static Logger logger = LoggerFactory.getLogger(AIOAsynClientHandler.class);

    private AsynchronousSocketChannel socketChannel;

    private String host;

    private int port;

    private CountDownLatch latch;

    public AIOAsynClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            socketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            logger.error("AIO client start failed",e);
        }

    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        socketChannel.connect(new InetSocketAddress(host,port),this,this);
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("latch timeout",e);
        }
        try {
            socketChannel.close();
        } catch (IOException e) {
            logger.error("socket channel close failed",e);
        }
    }

    @Override
    public void completed(Void result, AIOAsynClientHandler attachment) {
        byte[] bytes = "service hello".getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (byteBuffer.hasRemaining()){
                    socketChannel.write(byteBuffer,byteBuffer,this);
                } else {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            byte[] body = new byte[attachment.remaining()];
                            attachment.get(body);
                            String message = new String(body,StandardCharsets.UTF_8);
                            logger.info("service say is {}",message);
                            latch.countDown();
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                socketChannel.close();
                                latch.countDown();
                            } catch (IOException e) {
                                logger.info("socket close failed",e);
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    socketChannel.close();
                    latch.countDown();
                } catch (IOException e) {
                    logger.info("socket close failed",e);
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AIOAsynClientHandler attachment) {
        try {
            socketChannel.close();
            latch.countDown();
        } catch (IOException e) {
            logger.info("socket close failed",e);
        }
    }
}

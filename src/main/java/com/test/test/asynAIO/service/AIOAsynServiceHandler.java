package com.test.test.asynAIO.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AIOAsynServiceHandler implements Runnable {

    private int port;

    private CountDownLatch latch;

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    private final static Logger logger = LoggerFactory.getLogger(AIOAsynServiceHandler.class);

    public AIOAsynServiceHandler(int port) {
        this.port = port;
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            logger.info("AIO start success port is {}", this.port);
        } catch (IOException e) {
            logger.error("AIO start failed", e);
        }
    }


    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("latch timeout", e);
        }
    }

    private void doAccept() {
        asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }

    public AsynchronousServerSocketChannel getAsynchronousServerSocketChannel() {
        return asynchronousServerSocketChannel;
    }

    public AIOAsynServiceHandler setAsynchronousServerSocketChannel(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
        this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
        return this;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public AIOAsynServiceHandler setLatch(CountDownLatch latch) {
        this.latch = latch;
        return this;
    }
}

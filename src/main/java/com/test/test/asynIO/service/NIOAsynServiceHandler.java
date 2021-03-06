package com.test.test.asynIO.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务端
 */
public class NIOAsynServiceHandler implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(NIOAsynServiceHandler.class);

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    public NIOAsynServiceHandler(int port) {
        try {
            //多路复用器
            selector = Selector.open();
            //服务端监听
            serverSocketChannel = ServerSocketChannel.open();
            //非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 2);
            //多路复用器注册在监听端口，监听OP_ACCEPT事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("NIO service start success port is {}", port);
        } catch (IOException e) {
            logger.error("NIO service start failed", e);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey selectionKey;
                while (it.hasNext()) {
                    selectionKey = it.next();
                    it.remove();
                    try {
                        handleInput(selectionKey);
                    } catch (Exception e) {
                        if (selectionKey!=null) {
                            selectionKey.cancel();
                            if (selectionKey.channel()!=null) {
                                selectionKey.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("selector select is error", e);
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(this.selector, SelectionKey.OP_READ);
            }
            if (selectionKey.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    logger.info("client is say {}", body);
                    returnMessage(socketChannel, "service say hello\n");
                }else if (readBytes<0){
                    selectionKey.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void returnMessage(SocketChannel socketChannel, String message) throws IOException {
        if (message != null && message.trim().length() > 0) {
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
        }
    }
}

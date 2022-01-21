package com.test.test.asynIO.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NIOAsynClientHandler implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(NIOAsynClientHandler.class);

    private SocketChannel socketChannel;

    private Selector selector;

    private volatile boolean stop;

    private String host;

    private int port;

    public NIOAsynClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            logger.error("NIOAsynClientHandler init is failed",e);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            System.exit(1);
        }
        while (!stop) {
            try {
                selector.select(1000L);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectorIterator = selectionKeys.iterator();
                SelectionKey selectionKey;
                while(selectorIterator.hasNext()) {
                    selectionKey = selectorIterator.next();
                    selectorIterator.remove();
                    try {
                        handleInput(selectionKey);
                    } catch (Exception e) {
                        selectionKey.cancel();
                        if (selectionKey.channel()!=null) {
                            selectionKey.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("selector select error",e);
//                this.stop();
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            if (selectionKey.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    doWrite(socketChannel,"service hello");
                } else {
                    System.exit(1);
                }
            }
            if (selectionKey.isReadable()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes>0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String body = new String(bytes,StandardCharsets.UTF_8);
                    logger.info("service return message is {}",body);
                    this.stop();
                    selectionKey.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void stop(){
        this.stop = !stop;
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host,port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel,"connect success");
        } else {
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel,String sendMessage) throws IOException {
        byte[] message = sendMessage.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(message.length);
        byteBuffer.put(message);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        if (!byteBuffer.hasRemaining()) {
            logger.info("client send message success");
        }
    }
}

package com.test.test.synchroIO.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞IO服务
 */
public class SynchronizationService {

    private final static Logger logger = LoggerFactory.getLogger(SynchronizationService.class);

    public static void main(String[] args) {
        int port = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                new Thread(new SelfServerHandler(socket)).start();
            }
        } catch (IOException e) {
            logger.error("synchronizationService is error", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    logger.error("serverSocket close failed!", e);
                }
            }
        }
    }

}

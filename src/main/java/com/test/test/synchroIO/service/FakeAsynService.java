package com.test.test.synchroIO.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FakeAsynService {

    private final static Logger logger = LoggerFactory.getLogger(FakeAsynService.class);

    public static void main(String[] args) {
        int port = 8080;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            Socket socket = null;
            FakeSelfServiceHandlerExecutePool executePool = new FakeSelfServiceHandlerExecutePool(16,50);
            while(true) {
                socket = server.accept();
                executePool.execute(new SelfServerHandler(socket));
            }
        } catch (IOException e) {
            logger.error("fakeAsynService is failed",e);
        } finally {
            if (server!=null) {
                try {
                    server.close();
                    server = null;
                } catch (IOException e) {
                    logger.error("serverSocket close failed",e);
                }
            }
        }
    }

}

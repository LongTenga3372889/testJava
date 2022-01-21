package com.test.test.synchroIO.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 服务实现
 */
public class SelfServerHandler implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(SelfServerHandler.class);

    private Socket socket ;

    public SelfServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            String successMessage = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    break;
                }
                logger.info("client say is {}",body);
                out.println("service say is over");
            }
        } catch (IOException e) {
            logger.error("SelfServerHandler is error");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("bufferedReader is close failed!",e);
                }
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("socket is close failed!",e);
                }
            }
            socket = null;
        }
    }
}

package com.test.test.synchroIO.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 同步阻塞IO客户端
 */
public class SelfClient {

    private final static Logger logger = LoggerFactory.getLogger(SelfClient.class);

    public static void main(String[] args) {
        int port = 8080;
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            socket = new Socket("127.0.0.1", port);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("service hello");
            String result = bufferedReader.readLine();
            logger.info(result);
        } catch (IOException e) {
            logger.error("selfClient is error", e);
        } finally {
            if (printWriter != null) {
                printWriter.close();
                printWriter = null;
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error("bufferedReader close is failed",e);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("socket close is failed",e);
                }
            }
            socket = null;
        }
    }

}

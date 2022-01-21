package com.test.test.asynIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NIO服务端开启
 */
public class NIOAsyn {

    private final static Logger logger = LoggerFactory.getLogger(NIOAsyn.class);

    public static void main(String[] args) {
        int port = 8080;
        NIOAsynService nioAsynService = new NIOAsynService(port);
        new Thread(nioAsynService,"NIO-Service-1").start();
    }

}

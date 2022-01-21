package com.test.test.asynIO.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NIO服务端开启
 */
public class NIOAsynService {

    private final static Logger logger = LoggerFactory.getLogger(NIOAsynService.class);

    public static void main(String[] args) {
        int port = 8080;
        NIOAsynServiceHandler nioAsynService = new NIOAsynServiceHandler(port);
        new Thread(nioAsynService,"NIO-Service-1").start();
    }

}

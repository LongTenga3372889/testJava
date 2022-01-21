package com.test.test.asynIO.client;

public class NIOAsynClient {

    public static void main(String[] args) {
        int port = 8080;
        new Thread(new NIOAsynClientHandler("127.0.0.1",port),"client").start();
    }

}

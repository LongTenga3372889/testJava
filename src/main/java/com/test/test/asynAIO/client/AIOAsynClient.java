package com.test.test.asynAIO.client;

public class AIOAsynClient {

    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";
        new Thread(new AIOAsynClientHandler(host,port),"AIO-CLIENT-ONE").start();
    }

}

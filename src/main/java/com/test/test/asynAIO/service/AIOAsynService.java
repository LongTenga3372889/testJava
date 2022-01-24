package com.test.test.asynAIO.service;

public class AIOAsynService {

    public static void main(String[] args) {
        int port = 8080;
        AIOAsynServiceHandler aioAsynServiceHandler = new AIOAsynServiceHandler(port);
        new Thread(aioAsynServiceHandler,"AIOService-Thread").start();
    }

}

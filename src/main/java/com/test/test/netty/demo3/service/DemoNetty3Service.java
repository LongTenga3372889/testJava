package com.test.test.netty.demo3.service;

public class DemoNetty3Service {

    public static void main(String[] args) throws InterruptedException {
        int port = 8000;
        new MyNettyDemo3Service().bind(port);
    }

}

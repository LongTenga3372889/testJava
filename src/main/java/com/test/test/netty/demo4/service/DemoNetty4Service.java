package com.test.test.netty.demo4.service;

public class DemoNetty4Service {

    public static void main(String[] args) throws InterruptedException {
        int port = 8000;
        new MyNettyDemo4Service().bind(port);
    }

}

package com.test.test.netty.demo2.service;

public class DemoNetty2Service {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new DemoNetty2ServiceHandler().bind(port);
    }

}

package com.test.test.synchroIO.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 伪异步线程池
 */
public class FakeSelfServiceHandlerExecutePool {

    private ExecutorService executorService;

    public FakeSelfServiceHandlerExecutePool(int maxPoolSize, int queueSize) {
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize,120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(SelfServerHandler selfServerHandler) {
        executorService.execute(selfServerHandler);
    }
}

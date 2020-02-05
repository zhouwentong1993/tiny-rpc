package com.wentong.rpc.netty.transport;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 正在进行中，还没有结束的请求们
 */
public class InFlightRequests implements Closeable {

    // 存放请求的容器
    private Map<Integer, ResponseFuture> futureMap = new HashMap<>();

    // 最多只允许放 10 个请求
    private Semaphore semaphore = new Semaphore(10);

    // 往 futureMap 里放，如果超时，抛出 TimeoutException
    public void put(ResponseFuture responseFuture) throws TimeoutException, InterruptedException {
        if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    public void remove(ResponseFuture responseFuture) {
        futureMap.remove(responseFuture.getRequestId());
    }


    @Override
    public void close() throws IOException {

    }
}

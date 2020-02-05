package com.wentong.rpc.netty.transport;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 正在进行中，还没有结束的请求们
 */
public class InFlightRequests implements Closeable {

    // 存放请求的容器
    private Map<Integer, ResponseFuture> futureMap = new HashMap<>();

    // 最多只允许放 10 个请求
    private Semaphore semaphore = new Semaphore(10);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledFuture scheduledFuture;

    public InFlightRequests() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeout, 0, 10, TimeUnit.SECONDS);
    }

    // 往 futureMap 里放，如果超时，抛出 TimeoutException
    public void put(ResponseFuture responseFuture) throws TimeoutException, InterruptedException {
        if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(), responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    public void remove(ResponseFuture responseFuture) {
        ResponseFuture remove = futureMap.remove(responseFuture.getRequestId());
        // 释放信号量
        if (remove != null) {
            semaphore.release();
        }
    }

    private void removeTimeout() {
        long nowTimeOfNow = System.nanoTime();
        futureMap.entrySet().removeIf(entry -> {
            // 如果里面的请求耗时超过了 10s，则移除该请求，并释放一个信号量
            if (nowTimeOfNow - entry.getValue().getTimestamp() > TimeUnit.SECONDS.toNanos(10)) {
                semaphore.release();
                return true;
            } else {
                return false;
            }
        });
    }


    @Override
    public void close() throws IOException {
        scheduledExecutorService.shutdown();
        scheduledFuture.cancel(true);
    }
}

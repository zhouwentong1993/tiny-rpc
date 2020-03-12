package com.wentong.rpc.netty.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态生成唯一 requestId 工具类
 * 现在只能保证在一个 JVM 空间内唯一
 */
public final class RequestIdSupport {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static int nextId() {
        return atomicInteger.incrementAndGet();
    }
}

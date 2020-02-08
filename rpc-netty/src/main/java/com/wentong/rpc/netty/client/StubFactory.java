package com.wentong.rpc.netty.client;

import com.wentong.rpc.netty.transport.Transport;

/**
 * 创建桩的工厂
 */
public interface StubFactory {

    /**
     * 创建一个桩对象
     * @param transport 通信协议
     * @param clazz 桩 class
     * @return 包装好的桩对象
     */
    <T> T crateStub(Transport transport, Class<T> clazz);

}

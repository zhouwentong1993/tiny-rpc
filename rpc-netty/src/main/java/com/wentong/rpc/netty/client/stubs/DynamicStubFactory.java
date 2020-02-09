package com.wentong.rpc.netty.client.stubs;

import com.wentong.rpc.netty.client.StubFactory;
import com.wentong.rpc.netty.transport.Transport;

/**
 * 生成桩工厂类
 */
public class DynamicStubFactory implements StubFactory {



    @Override
    public <T> T crateStub(Transport transport, Class<T> clazz) {
        return null;
    }
}

package com.wentong.rpc;

import java.io.Closeable;
import java.net.URI;

/**
 * RPC 框架对外提供的接口
 */
public interface RpcAccessPoint extends Closeable {

    /**
     * 获取服务提供者实例
     * @param uri 服务提供者地址
     * @param serviceClass 服务提供者 Class 对象
     * @param <T> 服务提供者类型
     * @return 服务提供者实例
     */
    <T> T getService(URI uri, Class<T> serviceClass);

    /**
     * 注册服务，供服务端调用
     * @param service 服务对象
     * @param clazz 服务对象 class
     * @return 服务提供的 URI
     */
    <T> URI addService(T service, Class<T> clazz);

}

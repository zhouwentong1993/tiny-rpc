package com.wentong.rpc;

import java.net.URI;

/**
 * 注册中心
 */
public interface NamingService {

    /**
     * 服务注册接口，提供给服务端调用
     * @param uri 该服务端的地址
     * @param serviceName 服务名
     */
    void register(URI uri, String serviceName);

    /**
     * 查找对应服务的 URI
     * @param serviceName 待查询服务名称
     * @return 服务的 URI
     */
    URI getServiceURI(String serviceName);


}

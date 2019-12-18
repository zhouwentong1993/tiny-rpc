package com.wentong.customer;

import com.wentong.netty.NettyClient;
import com.wentong.socketrpc.HelloService;

public class ClientBootstrap {

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.init("127.0.0.1", 8001);
//        HelloService service = nettyClient.getBean(HelloService.class, "127.0.0.1", 8001);
//        service.sayHello("你好");
    }

}

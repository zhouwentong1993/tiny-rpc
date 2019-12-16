package com.wentong.socketrpc;

import java.util.concurrent.TimeUnit;

public class ServiceConsumer {

    public static void main(String[] args) throws Exception {
        HelloService helloService = SocketRpc.refer(HelloService.class, "127.0.0.1", 8999);
        for (int i = 0; i < 100; i++) {
            System.out.println(helloService.sayHello(String.valueOf(i)));
            TimeUnit.SECONDS.sleep(1);
        }
    }

}

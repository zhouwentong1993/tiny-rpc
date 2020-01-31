package com.wentong.socketrpc;

public class ServiceProvider {
    public static void main(String[] args) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        SocketRpc.export(helloService, 8999);
    }
}

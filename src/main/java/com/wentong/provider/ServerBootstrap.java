package com.wentong.provider;

import com.wentong.netty.NettyServer;

public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.start(8000);
    }

}

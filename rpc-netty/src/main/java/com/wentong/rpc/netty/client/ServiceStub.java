package com.wentong.rpc.netty.client;

import com.wentong.rpc.netty.transport.Transport;

public interface ServiceStub {

    void setTransport(Transport transport);

}

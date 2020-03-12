package com.wentong.rpc.netty.client.stubs;

import com.wentong.rpc.netty.client.RequestIdSupport;
import com.wentong.rpc.netty.serialize.SerializeSupport;
import com.wentong.rpc.netty.transport.Transport;
import com.wentong.rpc.netty.transport.command.Command;
import com.wentong.rpc.netty.transport.command.Header;
import com.wentong.rpc.netty.transport.command.ResponseCode;
import com.wentong.rpc.netty.transport.command.ResponseHeader;

import static com.wentong.rpc.netty.client.ServiceType.RPC_SERVICE_TYPE;

/**
 * 抽象的桩类，含有 Stub 的基本功能
 * 通过一个 Transport 发送一个请求
 */
public class AbstractStub {

    private Transport transport;

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    protected byte[] invokeRemote(RpcRequest rpcRequest) {
        Command request = new Command();
        Header requestHeader = new Header(RequestIdSupport.nextId(), 1, RPC_SERVICE_TYPE);
        request.setHeader(requestHeader);
        request.setPayload(SerializeSupport.serialize(rpcRequest));
        try {
            Command command = transport.send(request).get();
            ResponseHeader responseHeader = (ResponseHeader) command.getHeader();
            if (responseHeader.getCode() != ResponseCode.SUCCESS.getCode()) {
                throw new IllegalStateException();
            }
            return command.getPayload();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}

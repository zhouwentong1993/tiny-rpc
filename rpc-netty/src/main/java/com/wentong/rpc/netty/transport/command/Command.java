package com.wentong.rpc.netty.transport.command;

import java.util.Arrays;

/**
 * 请求和响应的抽象
 */
public class Command {
    private Header header;
    private byte[] payload;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Command{" +
                "header=" + header +
                ", payload=" + Arrays.toString(payload) +
                '}';
    }
}

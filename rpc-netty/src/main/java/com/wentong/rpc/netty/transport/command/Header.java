package com.wentong.rpc.netty.transport.command;

public class Header {
    // 标识唯一请求
    private int requestId;
    // 标识请求版本
    private int version;
    // 标识请求类型
    private int type;

    public Header(int requestId, int version, int type) {
        this.requestId = requestId;
        this.version = version;
        this.type = type;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Header{" +
                "requestId=" + requestId +
                ", version=" + version +
                ", type=" + type +
                '}';
    }
}

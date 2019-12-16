package com.wentong.codec;

import java.util.StringJoiner;

public class Response {
    private String type;
    private String requestId;
    private String payload;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Response.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("requestId='" + requestId + "'")
                .add("payload='" + payload + "'")
                .toString();
    }
}

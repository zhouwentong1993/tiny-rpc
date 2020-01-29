package com.wentong.netty;

import com.wentong.codec.Request;
import com.wentong.codec.Response;
import com.wentong.common.ClassStructure;
import com.wentong.common.CommonValue;
import com.wentong.utils.Util;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;

public class NettyServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        System.out.println("收到请求：" + msg);

        if (StringUtils.isNotBlank(response.getPayload()) && response.getPayload().startsWith(CommonValue.REQUEST_HEAD)) {
            ClassStructure classStructure = Util.parseMessage(response.getPayload());
            Class<?> aClass = Class.forName(classStructure.getClassName());
            Object o = aClass.newInstance();

            Method method = findMethod(classStructure, aClass);
            Object invoke = method.invoke(o, classStructure.getParam());
            Request request = new Request();
            request.setType("type");
            request.setRequestId("requestId");
            request.setPayload(invoke.toString());
            ctx.writeAndFlush(request);
        }
    }

    private Method findMethod(ClassStructure classStructure, Class<?> aClass) {
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals(classStructure.getMethodName())) {
                return declaredMethod;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端 channelActive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyServerHandler.channelReadComplete");
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("NettyServerHandler.close");
        super.close(ctx, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("NettyServerHandler.exceptionCaught");
        System.out.println("exception:" + cause);
        ctx.close();
    }
}

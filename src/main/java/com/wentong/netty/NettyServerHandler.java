package com.wentong.netty;

import com.wentong.common.ClassStructure;
import com.wentong.common.CommonValue;
import com.wentong.utils.Util;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("NettyServerHandler.channelRead");
        String message = (String) msg;
        System.out.println("收到请求：" + message);
        if (StringUtils.isNotBlank(message) && message.startsWith(CommonValue.REQUEST_HEAD)) {
            ClassStructure classStructure = Util.parseMessage(message);
            Class<?> aClass = Class.forName(classStructure.getClassName());
            Object o = aClass.newInstance();

            Method method = findMethod(classStructure, aClass);
            Object invoke = method.invoke(o, classStructure.getParam());
            System.out.println(invoke);
            ctx.writeAndFlush(invoke);

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

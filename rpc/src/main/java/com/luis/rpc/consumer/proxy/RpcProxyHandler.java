package com.luis.rpc.consumer.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author luis
 * @date 2020/11/12
 */
public class RpcProxyHandler extends ChannelInboundHandlerAdapter {
    private Object response;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("client is exception ");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg is: " + msg);
        response = msg;
    }

    public Object getResponse() {
        return response;
    }
}

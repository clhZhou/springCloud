package com.luis.rpc.registry;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author luis
 * @date 2020/11/12
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    // 1.根据一个包名将所有符合条件的class全部扫描处理出来，放到一个容器中
    // 2.给每一个对应的class起一个唯一的名字，作为服务名称，保存到一个容器中
    // 3.当客户端连接过来时候，就会获取协议内容 InvokerProtocol的对象
    // 4.要去注册好的容器中找到服务号条件的服务
    // 5.通过远程调用Provider得到返回结果，并回复给客户端


    /**
     * 客户端建立链接回调
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    /**
     * 客户端发生异常调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}

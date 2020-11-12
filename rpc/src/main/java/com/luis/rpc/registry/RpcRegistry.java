package com.luis.rpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


/**
 * @author luis
 * @date 2020/11/12
 */
public class RpcRegistry {
    private int port;

    public RpcRegistry(int port) {
        this.port = port;
    }

    public void start() {
        try {
            //ServerSocket、ServerSocketChannel
            //使用netty api 实现
            //Selector 主线程，Work 子线程
            //初始化主线程池
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            //初始化子线程池，对应客户端的处理逻辑
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootStrap = new ServerBootstrap();
            serverBootStrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //netty中所有的业务逻辑处理全部归总到了一个队列中
                            //包含了各种处理逻辑，在netty中有个封装
                            //封装成一个对象，无锁化串行任务队列
                            //Pipeline
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //处理逻辑的封装，有一定的执行顺序
                            //对自定义协议内容要进行编、解码；类似于解析HTTP协议
                            //编码
                            channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 0));
                            //解码
                            channelPipeline.addLast(new LengthFieldPrepender(4));
                            //实参处理
                            channelPipeline.addLast("encoder", new ObjectEncoder());
                            channelPipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            //前面的编解码是完成对数据的解析
                            //最后一步，执行自己的逻辑
                            // 1.对传入对象进行注册，对外提供服务的名字
                            // 2.服务位置进行登记
                            // 3.通过端口号，进行服务发布
                            channelPipeline.addLast(new RegistryHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //正式服务启动，相当于用一个死循环开始轮询
            ChannelFuture channelFuture = serverBootStrap.bind(this.port).sync();
            System.out.println("Rpc Registry start listen at " + this.port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        new RpcRegistry(8080).start();
    }
}

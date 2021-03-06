package com.luis.rpc.consumer.proxy;

import com.luis.rpc.protocol.InvokerProtocol;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author luis
 * @date 2020/11/12
 */
public class RpcProxy {
    public static <T> T create(Class<?> clazz) {
        MethodProxy methodProxy = new MethodProxy(clazz);
        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, methodProxy);
        return result;
    }

    //使用代理把本地调用变成网络调用


    private static class MethodProxy implements InvocationHandler {
        private Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else {
                return rpcInvoker(proxy, method, args);
            }
        }

        private Object rpcInvoker(Object proxy, Method method, Object[] args) {
            final RpcProxyHandler proxyHandler = new RpcProxyHandler();

            //先构造一个协议的内容-消息
            InvokerProtocol invokerProtocol = new InvokerProtocol();
            invokerProtocol.setClassName(this.clazz.getName());
            invokerProtocol.setMethodName(method.getName());
            invokerProtocol.setParames(method.getParameterTypes());
            invokerProtocol.setValues(args);
            //发送网络请求
            EventLoopGroup workGroup = new NioEventLoopGroup();
            Bootstrap client = new Bootstrap();
            try {
                client.group(workGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                //netty中所有的业务逻辑处理全部归总到了一个队列中
                                //包含了各种处理逻辑，在netty中有个封装
                                //封装成一个对象，无锁化串行任务队列
                                //Pipeline
                                ChannelPipeline channelPipeline = ch.pipeline();
                                //处理逻辑的封装，有一定的执行顺序
                                //对自定义协议内容要进行编、解码；类似于解析HTTP协议
                                //编码,注意LengthFieldBasedFrameDecoder的参数（Integer.MAX_VALUE,0,4,0,4）
                                channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
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
                                channelPipeline.addLast(proxyHandler);
                            }
                        });

                //设置同步sync，防止writeAndFlush乱序问题
                ChannelFuture future = client.connect("localhost", 8080).sync();
                future.channel().writeAndFlush(invokerProtocol).sync();

//                future.channel().writeAndFlush(invokerProtocol).sync().addListener(new GenericFutureListener<Future<? super Void>>() {
//                    @Override
//                    public void operationComplete(Future<? super Void> future) throws Exception {
//                        if(future.isSuccess()){
//                            System.out.println("数据写入成功！");
//                        }else {
//                            System.out.println("数据未能写入！");
//                            future.cause().printStackTrace();
//                        }
//                    }
//                });

//                future.channel().writeAndFlush(invokerProtocol).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);

                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workGroup.shutdownGracefully();
            }
            return proxyHandler.getResponse();
        }
    }
}

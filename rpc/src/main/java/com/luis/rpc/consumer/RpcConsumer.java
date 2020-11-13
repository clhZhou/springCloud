package com.luis.rpc.consumer;

import com.luis.rpc.api.IRpcHelloService;
import com.luis.rpc.api.IRpcService;
import com.luis.rpc.consumer.proxy.RpcProxy;
import com.luis.rpc.consumer.proxy.RpcProxy1;
import com.luis.rpc.provider.RpcHelloServiceImpl;
import com.luis.rpc.provider.RpcServiceImpl;

/**
 * @author luis
 * @date 2020/11/12
 */
public class RpcConsumer {
    public static void main(String[] args) {

//        IRpcService rpcService = new RpcServiceImpl();
//        System.out.println(rpcService.add(8, 2));
//        System.out.println(rpcService.sub(8, 2));
//        System.out.println(rpcService.mult(8, 2));
//        System.out.println(rpcService.div(8, 2));
//
//        IRpcHelloService rpcHelloService = new RpcHelloServiceImpl();
//        System.out.println(rpcHelloService.hello("luis"));


        IRpcService rpcService = RpcProxy1.create(IRpcService.class);
        System.out.println(rpcService.add(8, 2));
        System.out.println(rpcService.sub(8, 2));
        System.out.println(rpcService.mult(8, 2));
        System.out.println(rpcService.div(8, 2));

        IRpcHelloService rpcHelloService = RpcProxy1.create(IRpcHelloService.class);
        System.out.println(rpcHelloService.hello("luis"));
    }

}

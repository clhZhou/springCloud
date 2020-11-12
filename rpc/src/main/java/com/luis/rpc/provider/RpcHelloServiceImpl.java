package com.luis.rpc.provider;

import com.luis.rpc.api.IRpcHelloService;

/**
 * @author luis
 * @date 2020/11/12
 */
public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello" + name;
    }
}

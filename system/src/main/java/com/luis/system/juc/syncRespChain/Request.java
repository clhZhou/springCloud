package com.luis.system.juc.syncRespChain;

/**
 * @author luis
 * @date 2020/12/28
 */
public class Request {
    private String name;

    public Request(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                '}';
    }
}

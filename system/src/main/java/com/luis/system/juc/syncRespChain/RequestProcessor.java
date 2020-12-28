package com.luis.system.juc.syncRespChain;

/**
 * @author luis
 * @date 2020/12/28
 */
public interface RequestProcessor {

    void requestProcessor(Request request);

    void shutdown();
}

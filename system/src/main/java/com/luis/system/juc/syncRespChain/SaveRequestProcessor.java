package com.luis.system.juc.syncRespChain;

import java.lang.invoke.VolatileCallSite;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author luis
 * @date 2020/12/28
 */
public class SaveRequestProcessor extends Thread implements RequestProcessor {

    RequestProcessor nextProcessor;

    //存储Request

    BlockingDeque<Request> requests = new LinkedBlockingDeque<>();


    public SaveRequestProcessor(RequestProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public void requestProcessor(Request request) {
        requests.add(request);
    }

    @Override
    public void shutdown() {
        System.out.println("SaveProcessor begin shutdown");
        requests.clear();
        Thread.currentThread().interrupt();
        nextProcessor.shutdown();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                //阻塞式请求
                Request request = requests.take();
                System.out.println("Save:" + request);
                //传递给下一个处理器
                nextProcessor.requestProcessor(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}

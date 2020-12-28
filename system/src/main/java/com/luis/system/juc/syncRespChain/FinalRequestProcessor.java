package com.luis.system.juc.syncRespChain;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author luis
 * @date 2020/12/28
 */
public class FinalRequestProcessor extends Thread implements RequestProcessor {


    RequestProcessor nextProcessor;

    //存储Request

    BlockingDeque<Request> requests = new LinkedBlockingDeque<>();


    @Override
    public void requestProcessor(Request request) {
        requests.add(request);
    }

    @Override
    public void shutdown() {
        System.out.println("FinalProcessor begin shutdown");
        requests.clear();
        Thread.interrupted();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                //阻塞式请求
                Request request = requests.take();
                System.out.println("Final:" + request);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            }
        }
    }
}

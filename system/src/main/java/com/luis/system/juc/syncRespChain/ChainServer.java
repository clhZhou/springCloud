package com.luis.system.juc.syncRespChain;

/**
 * @author luis
 * @date 2020/12/28
 */
public class ChainServer {

    private RequestProcessor firstProcessor;

    private void setupRequestProcessor(){
        RequestProcessor finalProcessor = new FinalRequestProcessor();
        RequestProcessor saveProcessor = new SaveRequestProcessor(finalProcessor);
        firstProcessor = new PrintRequestProcessor(saveProcessor);
        ((FinalRequestProcessor)finalProcessor).start();
        ((SaveRequestProcessor)saveProcessor).start();
        ((PrintRequestProcessor)firstProcessor).start();
    }

    public void startup(){
        setupRequestProcessor();
    }

    public void shutdown(){
        firstProcessor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        ChainServer chainServer = new ChainServer();
        chainServer.startup();
        Request request = new Request("Zr");
        chainServer.firstProcessor.requestProcessor(request);
        Thread.sleep(5000);
        chainServer.shutdown();
    }

}

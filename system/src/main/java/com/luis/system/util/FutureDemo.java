package com.luis.system.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author luis
 * @date 2020/12/13
 */
public class FutureDemo implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "hello luis";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureDemo futureDemo = new FutureDemo();
        FutureTask futureTask = new FutureTask(futureDemo);
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }
}

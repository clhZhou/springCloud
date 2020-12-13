package com.luis.system.util;

import java.util.concurrent.*;

/**
 * @author luis
 * @date 2020/12/13
 */
public class Demo implements Runnable{
    public static void main(String[] args) {
        //固定线程数量
        ExecutorService executorService = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Demo());
        }
        //只有一个核心线程的线程池
        Executors.newSingleThreadExecutor();
        //缓存线程池
        Executors.newCachedThreadPool();
        //延迟执行线程池,心跳检测
        Executors.newScheduledThreadPool(1);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

package com.luis.register.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author luis
 * @date 2020/11/30
 */
public class LockDemo {

    //curator zk解决分布式锁的解决方案
    //解决惊群效应， watch机制

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.0.200:2181")
                .sessionTimeoutMs(5000)
                //重试策略
                //衰减重试、重试一次 4种重试策略
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();


        final InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/locks");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "->尝试竞争锁");
                try {
                    lock.acquire();//阻塞竞争锁
                    System.out.println(Thread.currentThread().getName() + "->成功获得了锁");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();//释放锁
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "Thread " + i).start();
        }


    }
}

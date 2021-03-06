package com.luis.system.juc.model;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author luis
 * @date 2020/12/27
 */
public class Provider implements Runnable{

    public Queue<String> msg;

    public int size;

    public Lock lock;

    public Condition condition;

    public Provider(Queue<String> msg, int size, Lock lock, Condition condition) {
        this.msg = msg;
        this.size = size;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        int i = 0;
        while (true){
            i++;
//            System.out.println(Thread.currentThread().getName()+" 抢占到了锁");
            lock.lock();
            try {
                while (msg.size()==size){
                    System.out.println("生产者队列满了，先等待");
                    try {
                        condition.await();//阻塞线程并释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"生产消息："+i);
                msg.add(""+i);
                condition.signal();//唤醒阻塞状态下的线程
            }finally {
                lock.unlock();
//                System.out.println(Thread.currentThread().getName()+" 释放了锁");
            }
        }
    }
}

package com.luis.system.juc.model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author luis
 * @date 2020/12/27
 */
public class Main {

    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        int size = 5;
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread thread = new Thread(new Provider(queue,size,lock,condition),"provider");
        Thread thread1 = new Thread(new Consumer(queue,size,lock,condition),"consumer");
        thread.start();
        thread1.start();
    }
}

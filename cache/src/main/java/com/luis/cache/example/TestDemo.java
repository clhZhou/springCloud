package com.luis.cache.example;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author luis
 * @date 2020/12/12
 */
public class TestDemo implements Runnable{
    @Override
    public void run() {
        System.out.println("is run...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        demo1 d1 = new demo1();
        demo2 d2 = new demo2();
        Thread t1 = new Thread(()->{
            synchronized (d1){
                d1.run();
                synchronized (d2){
                    d2.run();
                }
            }
        },"t1");
//        t1.start();


        Thread t2 = new Thread(()->{
            synchronized (d2){
                d2.run();
                synchronized (d1){
                    d1.run();
                }
            }
        },"t2");
//        t2.start();

        //反射
        try {
            Class clazz = Class.forName("com.luis.cache.example.demo11");
            Class superClazz = clazz.getSuperclass();
            Object obj = superClazz.newInstance();
            Method method = superClazz.getMethod("setId",int.class);
            method.invoke(obj,1);
            Method method1 = superClazz.getMethod("getId");
            System.out.println(method1.invoke(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

class demo1 implements Runnable{
    int id ;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("run is demo1");
    }
}

class demo2 implements Runnable{
    @Override
    public void run() {
        System.out.println("run is demo2");
    }
}

class demo11 extends demo1{

}

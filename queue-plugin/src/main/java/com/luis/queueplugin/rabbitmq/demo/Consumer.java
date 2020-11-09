package com.luis.queueplugin.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author luis
 * @date 2020/11/8
 * https://www.cnblogs.com/chy18883701161/p/12501428.html
 */
public class Consumer {
    private final static String QUEUE_NAME = "my_queue"; //队列名称

    public static void receive() throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.200"); //设置rabbitmq-server的地址
        connectionFactory.setPort(5672);  //使用的端口号
        connectionFactory.setVirtualHost("/");  //使用的虚拟主机
        connectionFactory.setUsername("admin"); //登录用户名
        connectionFactory.setPassword("admin"); //登录密码


        //由连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //通过连接创建信道
        Channel channel = connection.createChannel();

        //创建消费者，指定要使用的channel。QueueingConsume类已经弃用，使用DefaultConsumer代替
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //监听的queue中有消息进来时，会自动调用此方法来处理消息。但此方法默认是空的，需要重写
            @Override
            public void handleDelivery(java.lang.String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                java.lang.String msg = new java.lang.String(body);
                System.out.println("received msg: " + msg);
            }
        };

        //监听指定的queue。会一直监听。
        //参数：要监听的queue、是否自动确认消息、使用的Consumer
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }

    public static void main(String[] args) {
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
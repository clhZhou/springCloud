package com.luis.register.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author luis
 * @date 2020/11/30
 */
public class LeaderSelectorClient extends LeaderSelectorListenerAdapter implements Closeable {

    private String name;
    private LeaderSelector leaderSelector;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public LeaderSelectorClient(){

    }

    public LeaderSelector getLeaderSelector() {
        return leaderSelector;
    }

    public void setLeaderSelector(LeaderSelector leaderSelector) {
        this.leaderSelector = leaderSelector;
    }

    public LeaderSelectorClient(String name) {
        this.name = name;

//        leaderSelector.autoRequeue();//自动重复参与选举
    }

    public void start() {
        leaderSelector.start();//开始竞争leader
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        //如果进入当前方法，意味着当前的进程获得了锁，获得锁后，这个方法会回调
        //方法执行结束后，表示释放了锁
        System.out.println(name + "->>现在是leader了");
        countDownLatch.await();//阻塞当前的进程，防止leader丢失
    }


    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }

    public static void main(String[] args) throws IOException {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.0.200:2181")
                .sessionTimeoutMs(5000)
                //重试策略
                //衰减重试、重试一次 4种重试策略
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        LeaderSelectorClient leaderSelectorClient = new LeaderSelectorClient("ClientA");
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, "/leader", leaderSelectorClient);
        leaderSelectorClient.setLeaderSelector(leaderSelector);
        leaderSelectorClient.start();//开始选举
        System.in.read();
    }
}

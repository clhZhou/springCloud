package com.luis.register.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author luis
 * @date 2020/11/29
 */
public class CuratorDemo {

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.0.200:2181")
                .sessionTimeoutMs(5000)
                //重试策略
                //衰减重试、重试一次 4种重试策略
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
        //crud

        try {
//            createData(curatorFramework);
//            updateData(curatorFramework);
//            queryData(curatorFramework);
            deleteData(curatorFramework);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建节点
     * @param curatorFramework
     * @throws Exception
     */
    public static void createData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath("/data/program","test".getBytes());
    }


    /**
     * 修改节点
     * @param curatorFramework
     * @throws Exception
     */
    public static void updateData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/data/program","update".getBytes());
    }

    /**
     * 删除节点
     * @param curatorFramework
     * @throws Exception
     */
    public static void deleteData(CuratorFramework curatorFramework) throws Exception {
        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath("/data/program");
        curatorFramework.delete().withVersion(stat.getVersion()).forPath("/data/program");
    }

    /**
     * 查询节点
     * @param curatorFramework
     * @throws Exception
     */
    public static void queryData(CuratorFramework curatorFramework) throws Exception {
        String value = new String(curatorFramework.getData().forPath("/data/program"));
        System.out.println(value);
    }




}

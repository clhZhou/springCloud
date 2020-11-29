package com.luis.register.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luis
 * @date 2020/11/29
 */
public class ACLDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.0.200:2181")
                .sessionTimeoutMs(5000)
                //重试策略
                //衰减重试、重试一次 4种重试策略
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
        //注入权限
        List<ACL> list = new ArrayList<>();
        ACL acl = new ACL(ZooDefs.Perms.READ|ZooDefs.Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
        list.add(acl);
        curatorFramework.setACL().withACL(list).forPath("/temp");
//        curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/auth");
    }






}

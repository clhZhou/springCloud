package com.luis.register.curator;

import com.sun.xml.internal.ws.util.xml.NodeListIterator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author luis
 * @date 2020/11/29
 */
public class WatchDemo {

    public static void main(String[] args) throws Exception {
        //PathChildCache 针对于子节点的变化 触发事件
        //NodeCache 针对当前节点的变化 触发事件
        //TreeCache 是上述两种变化的综合事件

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.0.200:2181")
                .sessionTimeoutMs(5000)
                //重试策略
                //衰减重试、重试一次 4种重试策略
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();

//        addListenerWithNode(curatorFramework);
//        addListenerWithChild(curatorFramework);
        addListerWithTree(curatorFramework);
        System.in.read();

    }

    /**
     * 当前节点的变更时候触发
     * @param curatorFramework
     * @throws Exception
     */
    private static void addListenerWithNode(CuratorFramework curatorFramework) throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework,"/watch",false);
        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("receive Node Changed");
                System.out.println(nodeCache.getCurrentData().getPath() +"->>>"+new String(nodeCache.getCurrentData().getData()));
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }


    /**
     * 子节点变更时触发
     * @param curatorFramework
     * @throws Exception
     */
    private static void addListenerWithChild(CuratorFramework curatorFramework) throws Exception {
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework,"/watch",true);
        PathChildrenCacheListener nodeCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("child Node Changed");
                System.out.println(pathChildrenCacheEvent.getType()+"->>>"+new String(pathChildrenCacheEvent.getData().getData()));
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);
    }

    /**
     * 节点变更触发事件综合（包含当前节点、子节点）
     * @param curatorFramework
     * @throws Exception
     */
    private static void addListerWithTree(CuratorFramework curatorFramework) throws Exception {
        TreeCache treeCache = new TreeCache(curatorFramework,"/watch");
        TreeCacheListener treeCacheListener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println("tree Node Changed");
                System.out.println(treeCacheEvent.getType() + "->>>" + new String(treeCacheEvent.getData().getPath()));
            }
        };
        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();
    }
}

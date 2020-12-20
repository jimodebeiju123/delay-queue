//package com.ecs.redis.handler;
//
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.framework.recipes.cache.PathChildrenCache;
//import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
//import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
//import org.apache.curator.retry.RetryNTimes;
//import org.apache.zookeeper.*;
//import org.apache.zookeeper.data.Stat;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//
///**
// * zookeeper 工具类
// * @author zhanglinfeng
// */
//public class ZookeeperHandler {
//
//
//    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
//
//    public static void main1(String[] args) throws IOException, InterruptedException, KeeperException {
//        ZooKeeper zk = new ZooKeeper("localhost:2181", 20000, new Watcher() {
//            @Override
//            public void process(WatchedEvent watchedEvent) {
//                if(Event.KeeperState.SyncConnected
//                        .equals( watchedEvent.getState())&& Event.EventType.None.equals(watchedEvent.getType())){
//                    countDownLatch.countDown();
//                    System.out.println("zookeeper连接上");
//                }
//            }
//        });
//        countDownLatch.await();
//
//        if(zk.exists("/test",true) == null){
//            String mm = zk.create("/test", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//            System.out.println(mm);
//        }
//        String msg = new String(zk.getData("/test",false,null));
//        System.out.println("获取到的数据为:"+msg);
//        zk.delete("/test",-1);
//    }
//
//    public static void main(String[] args) throws Exception {
//        CuratorFramework client = CuratorFrameworkFactory.builder()
//                .connectString("localhost:2181")
//                .retryPolicy( new RetryNTimes(10, 5000))
//                .namespace("QUEUE").build();
//        client.start();
//        System.out.println("已经连接上zookeeper");
//
//
//        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/TEST",true);
//        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
//        pathChildrenCache.getListenable().addListener((client1, event) -> System.out.println("事件类型："  + event.getType() + "；操作节点：" + event.getData().getPath()));
//
//        //创建一个临时节点
//        if(client.checkExists().forPath("/TEST/111111") == null){
//            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/TEST/111111");
//        }
//
//
//        //获取所有子节点
//        List<String> paths = client.getChildren().forPath("/TEST");
//        System.out.println(paths);
//
//
//    }
//}

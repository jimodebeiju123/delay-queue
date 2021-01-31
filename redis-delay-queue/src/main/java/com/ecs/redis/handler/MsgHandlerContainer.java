/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ecs.redis.handler;

import com.ecs.redis.api.ConsumeMsgCallBack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理容器
 *
 * @author zhanglinfeng
 */
public class MsgHandlerContainer {

    /**
     * 保存队列与消息处理的类
     */
    private static Map<String, ConsumeMsgCallBack> consumeHandlerMap = new HashMap<>(10);

    private static Map<String, CompletableFuture<Void>> consumeQueue = new ConcurrentHashMap<>(10);


    /**
     * 增加处理类
     *
     * @param queueName
     * @param consumeMsgCallBack
     */
    public static void addhandler(String queueName, ConsumeMsgCallBack consumeMsgCallBack) {
        consumeHandlerMap.putIfAbsent(queueName, consumeMsgCallBack);
    }

    /**
     * 获取当前节点可以处理的所有队列,用来做消息转移和消费处理
     * @return
     */
    public static Set<String> getAllQueueName(){
        return consumeHandlerMap.keySet();
    }

    /**
     * 获取处理类
     *
     * @param queueName
     * @return
     */
    public static ConsumeMsgCallBack getHandler(String queueName) {
        return consumeHandlerMap.get(queueName);
    }


    /**
     * 保存已经启动的获取消息线程
     *
     * @param queueName
     * @param future
     */
    public static void addConsumeQueue(String queueName, CompletableFuture<Void> future) {
        consumeQueue.putIfAbsent(queueName, future);
    }

    /**
     * 获取已经保存的获取消息的线程
     *
     * @param queueName
     * @return
     */
    public static CompletableFuture<Void> getConsumeQueue(String queueName) {
        return consumeQueue.get(queueName);
    }

    /**
     * 判断获取消息的线程是否已经存在
     *
     * @param queueName
     * @return
     */
    public static boolean checkConsumeQueue(String queueName) {
        return consumeQueue.containsKey(queueName);
    }
}

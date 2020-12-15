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

import com.ecs.redis.enums.BroadcastType;
import com.ecs.redis.service.ConsumeMsgService;
import com.ecs.redis.service.TransferMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 消息处理类
 *
 * @author zhanglinfeng
 */
public class MessageHandlerTask {

    private Logger logger = LoggerFactory.getLogger(MessageHandlerTask.class);

    private RedisTemplate<String, String> redisTemplate;

    private TransferMsgService transferMsgService;


    private ConsumeMsgService consumeMsgService;

    /**
     * 初始化操作
     *
     * @param redisTemplate
     * @param transferMsgService
     * @param consumeMsgService
     */
    public MessageHandlerTask(RedisTemplate<String, String> redisTemplate, TransferMsgService transferMsgService, ConsumeMsgService consumeMsgService) {
        this.redisTemplate = redisTemplate;
        this.transferMsgService = transferMsgService;
        this.consumeMsgService = consumeMsgService;
    }

    /**
     * 消息转移处理
     */
    public void transferMsgs() {
        //获取所有的队列
        Set<String> allQueue = MsgHandlerContainer.getAllQueueName();
        if (CollectionUtils.isEmpty(allQueue)) {
            logger.debug("执行消息转移任务------->当前没有队列，不作任何处理");
            return;
        }
        //获取当前时间
        Long time = System.currentTimeMillis();
        allQueue.forEach(key ->
                transferMsgService.transferMsg(key, time, BroadcastType.COLONY));
    }

    /**
     * 消费处理
     */
    public void consumeMsg() {
        Set<String> allQueue = MsgHandlerContainer.getAllQueueName();
        if (CollectionUtils.isEmpty(allQueue)) {
            logger.debug("执行消息消费任务------->当前没有队列，不作任何处理");
            return;
        }
        allQueue.forEach(key -> {
            if (!MsgHandlerContainer.checkConsumeQueue(key)) {
                MsgHandlerContainer.addConsumeQueue(key, CompletableFuture.runAsync(() -> {
                    logger.debug("开始执行消息消费");
                    while (true) {
                        consumeMsgService.consumeMsg(key);
                    }
                }));
            }
        });
    }


    /**
     * 将未消费(无法消费) 的消息重新放入队列
     */
    public void reInsertMsg() {
        Set<String> allQueue = MsgHandlerContainer.getAllQueueName();
        if (CollectionUtils.isEmpty(allQueue)) {
            logger.debug("执行消息重新放入队列------->当前没有队列，不作任何处理");
            return;
        }
        Long time = System.currentTimeMillis();
        allQueue.forEach(key -> consumeMsgService.reInsertMsg(key, time));
    }

}

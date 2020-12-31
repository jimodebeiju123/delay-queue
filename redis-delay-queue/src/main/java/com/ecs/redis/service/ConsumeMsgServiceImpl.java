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
package com.ecs.redis.service;

import com.ecs.redis.api.ConsumeMsgCallBack;
import com.ecs.redis.constant.QueueConfig;
import com.ecs.redis.enums.ConsumeOrderlyStatus;
import com.ecs.redis.handler.MsgHandlerContainer;
import com.ecs.redis.utils.KeysUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanglinfeng
 */
public class ConsumeMsgServiceImpl implements ConsumeMsgService {
    private Logger logger = LoggerFactory.getLogger(ConsumeMsgServiceImpl.class);

    private RedisTemplate<String, String> redisTemplate;

    private QueueConfig queueConfig;


    /**
     * 初始化操作
     *
     * @param redisTemplate
     * @param queueConfig
     */
    public ConsumeMsgServiceImpl(RedisTemplate<String, String> redisTemplate, QueueConfig queueConfig) {
        this.redisTemplate = redisTemplate;
        this.queueConfig = queueConfig;
    }

    /**
     * 消费消息
     *
     * @param queueName
     */
    @Override
    public void consumeMsg(String queueName) {
        //获取自身处理节点
        try {
            String handlerNodeHostName = KeysUtils.getHandlerNodeHostName(queueName);
            String message = redisTemplate.opsForList()
                    .rightPop(handlerNodeHostName, queueConfig.getConsumeBlocksTime(), TimeUnit.SECONDS);
            if (!StringUtils.isEmpty(message)) {
                String[] allMsg = message.split(",");
                String msg = allMsg[0];
                ConsumeMsgCallBack callBack = MsgHandlerContainer.getHandler(queueName);
                if (callBack != null) {
                    ConsumeOrderlyStatus consumeOrderlyStatus = callBack.consumeMsg(msg);
                    if (ConsumeOrderlyStatus.SUCCESS.equals(consumeOrderlyStatus)) {
                        //处理成功
                        return;
                    }
                    Long num = redisTemplate.opsForValue().increment("queue_num:"+message.hashCode());
                    //重试
                    if ((num == null ? 0L : num) < queueConfig.getRetryNum()) {
                        redisTemplate.opsForList().rightPush(handlerNodeHostName, message);
                        return ;
                    }
                    //超过重试次数则丢弃
                    redisTemplate.delete("queue_num:"+message.hashCode());
                }
            }
        } catch (Exception e) {
            logger.error("消息处理失败！连接已经超时", e);
        }

    }

    /**
     * 将消息重新放回队列
     *
     * @param queueName
     * @param time
     */
    @Override
    public void reInsertMsg(String queueName, Long time) {
        List<String> keys = Lists.newArrayList(KeysUtils.getHandlerhost(queueName), queueName);
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("lua/re_insert.lua"));
        logger.debug("开始执行队列:[{}]的重新插入", queueName);
        redisTemplate.execute(defaultRedisScript, keys, time + "");
    }
}

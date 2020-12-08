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
package com.ecs.redis.bean;

import com.ecs.redis.RedisQueueAutoConfigure;
import com.ecs.redis.constant.QueueConfig;
import com.ecs.redis.handler.MessageHandlerTask;
import com.ecs.redis.service.ConsumeMsgService;
import com.ecs.redis.service.ConsumeMsgServiceImpl;
import com.ecs.redis.service.TransferMsgService;
import com.ecs.redis.service.TransferMsgServiceImpl;
import com.ecs.redis.task.MessageTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 消息消费
 *
 * @author zhanglinfeng
 */
@Configuration
@ConditionalOnBean(RedisQueueAutoConfigure.class)
@ConditionalOnProperty(prefix = "redis.queue", value = "consume-enabled", havingValue = "true")
public class RedisQueueConsumeConfig {


    /**
     * 初始化消息消费类
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsumeMsgService consumeMsgService(
            RedisTemplate<String, String> redisTemplate,
            QueueConfig queueConfig) {
        return new ConsumeMsgServiceImpl(redisTemplate, queueConfig);
    }


    /**
     * 初始化消息任务
     *
     * @param redisTemplate
     * @param transferMsgService
     * @param consumeMsgService
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageHandlerTask messageHandlerTask(RedisTemplate<String, String> redisTemplate,
                                                 TransferMsgService transferMsgService,
                                                 ConsumeMsgService consumeMsgService) {
        return new MessageHandlerTask(redisTemplate, transferMsgService, consumeMsgService);
    }

    /**
     * 初始化消息转移服务
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public TransferMsgService transferMsgService(RedisTemplate<String, String> redisTemplate) {
        return new TransferMsgServiceImpl(redisTemplate);
    }


    /**
     * 初始化定时任务的处理
     *
     * @param messageHandlerTask
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageTask messageTask(MessageHandlerTask messageHandlerTask) {
        return new MessageTask(messageHandlerTask);
    }

}

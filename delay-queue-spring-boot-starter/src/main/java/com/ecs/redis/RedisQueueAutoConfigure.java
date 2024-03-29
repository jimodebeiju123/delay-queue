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
package com.ecs.redis;


import com.ecs.redis.constant.QueueConfig;
import com.ecs.redis.handler.MessageHandlerTask;
import com.ecs.redis.handler.QueueContextRefreshedListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * redis 延迟队列自动装配
 *
 * @author zhanglinfeng
 */
@Configuration
@EnableScheduling
@ConditionalOnClass(MessageHandlerTask.class)
@EnableConfigurationProperties(RedisQueueProperties.class)
@ConditionalOnProperty(prefix = "redis.queue", value = "enabled", havingValue = "true")
public class RedisQueueAutoConfigure {


    /**
     * 初始化队列配置
     *
     * @param redisQueueProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public QueueConfig queueConfig(RedisQueueProperties redisQueueProperties) {
        QueueConfig queueConfig = new QueueConfig();
        queueConfig.setConsumeBlocksTime(redisQueueProperties.getConsumeBlocksTime());
        queueConfig.setRetryNum(redisQueueProperties.getRetryNum());
        return queueConfig;
    }


    /**
     * 初始化扫描类
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public QueueContextRefreshedListener queueContextRefreshedListener() {
        return new QueueContextRefreshedListener();
    }


    /**
     * 定时任务处理线程
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(20);
        return threadPoolTaskScheduler;
    }
}

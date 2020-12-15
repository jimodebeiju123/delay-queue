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

import com.ecs.redis.enums.BroadcastType;
import com.ecs.redis.utils.KeysUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

/**
 * @author zhanglinfeng
 */
public class TransferMsgServiceImpl implements TransferMsgService {
    private Logger logger = LoggerFactory.getLogger(TransferMsgServiceImpl.class);


    private RedisTemplate<String, String> redisTemplate;

    /**
     * 初始化操作
     * @param redisTemplate
     */
    public TransferMsgServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 将queueName 里面大于等于time的消息转移到处理队列里面
     *
     * @param queueName
     * @param time
     */
    @Override
    public void transferMsg(String queueName, Long time, BroadcastType broadcastType) {
        logger.debug("执行队列:[{}]大于[{}]的消息的转移", queueName, time);
        String queueNodeHostName = KeysUtils.getQueueNodeKey(queueName);
        List<String> keys = Lists.newArrayList(queueName, queueNodeHostName);
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("lua/move_queue"));
        //1.获取出当前需要处理的消息，将消息放入到处理队列中。如果没有处理队列，则将自己放入处理队列
        //传入参数，key1 = 队列名称，key2= 该队列的所有处理节点， ARGV1 = 当前时间，ARGV2 = 随机获取的处理节点，ARGV3 = 当前节点
        redisTemplate.execute(defaultRedisScript, keys, time+"",
                obtainClientNode(queueNodeHostName), KeysUtils.getHandlerNodeHostName(queueName));
    }

    /**
     * 随机获取处理节点
     *
     * @param queueNodeHostName
     */
    private String obtainClientNode(String queueNodeHostName) {
        return redisTemplate.opsForSet().randomMember(queueNodeHostName);
    }
}

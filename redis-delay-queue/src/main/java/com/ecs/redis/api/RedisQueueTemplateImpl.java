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
package com.ecs.redis.api;

import com.ecs.redis.utils.KeysUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;
import java.util.Set;

/**
 * 发送消息
 *
 * @author zhanglinfeng
 */
public class RedisQueueTemplateImpl implements RedisQueueTemplate<String> {
    private final Logger logger = LoggerFactory.getLogger(RedisQueueTemplateImpl.class);

    private RedisTemplate<String, String> redisTemplate;

    public RedisQueueTemplateImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发送延迟消息
     *
     * @param queueName
     * @param value
     * @param time
     */
    @Override
    public void sendMsg(String queueName, String value, Long time) {
        logger.debug("开始发送消息[{}]到队列:[{}],消息延迟到:[{}]处理", value, queueName, time);
        List<String> keys = Lists.newArrayList(KeysUtils.getQueueKey(queueName), KeysUtils.getQueueSetKey());
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("lua/add_queue.lua"));
        redisTemplate.execute(defaultRedisScript, keys, value, time + "");
    }

    /**
     * 发送延迟消息啊
     *
     * @param queueName 队列名称
     * @param value     值
     * @param delayTime 延迟的时间，单位为秒
     */
    @Override
    public void sendMsgByDelayTime(String queueName, String value, Long delayTime) {
        long time = System.currentTimeMillis() + (delayTime == null ? 0L : delayTime * 1000);
        sendMsg(queueName, value, time);
    }

    /**
     * 发送多个延迟消息
     *
     * @param queueName
     * @param typedTuples
     */
    @Override
    public void sendMsgs(String queueName, Set<ZSetOperations.TypedTuple<String>> typedTuples) {
        logger.debug("开始发送多个消息到队列:[{}]", queueName);
        String key = KeysUtils.getQueueKey(queueName);
        redisTemplate.opsForZSet().add(key, typedTuples);
        redisTemplate.opsForSet().add(KeysUtils.getQueueSetKey(), key);
    }

}

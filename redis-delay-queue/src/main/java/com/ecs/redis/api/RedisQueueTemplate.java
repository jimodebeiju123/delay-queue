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

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * 延迟队列发送模版
 * @author zhanglinfeng
 */
public interface RedisQueueTemplate<V> {


    /**
     * 发送延迟消息
     * @param queueName 队列名称
     * @param value 值
     * @param time 延迟到的时间 unix时间戳
     */
    void sendMsg(String queueName,V value,Long time);


    /**
     * 发送延迟消息啊
     * @param queueName 队列名称
     * @param value 值
     * @param delayTime 延迟的时间，单位为秒
     */
    void sendMsgByDelayTime(String queueName,V value,Long delayTime);

    /**
     * 发送多个延迟消息
     * @param queueName
     * @param tuples
     */
    void sendMsgs(String queueName, Set<ZSetOperations.TypedTuple<V>> tuples);

}

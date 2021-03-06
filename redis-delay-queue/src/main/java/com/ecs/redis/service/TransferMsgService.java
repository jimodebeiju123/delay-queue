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

/**
 * 转移消息的任务
 * @author zhanglinfeng
 */
public interface TransferMsgService {

    /**
     * 将queueName 里面大于等于time的消息转移到处理队列里面
     * @param queueName
     * @param time
     * @param broadcastType 广播类型
     */
    void transferMsg(String queueName, Long time, BroadcastType broadcastType);

    /**
     * 删除 队列中的处理节点
     * @param queueName
     */
    void deleteHostNameNode(String queueName);
}

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

/**
 * 消费消息
 *
 * @author zhanglinfeng
 */
public interface ConsumeMsgService {



    /**
     * 消费消息
     * @param queueName
     */
    void consumeMsg(String queueName);

    /**
     * 将消息重新放回队列
     * @param queueName
     * @param time
     */
    void reInsertMsg(String queueName,Long time);

}

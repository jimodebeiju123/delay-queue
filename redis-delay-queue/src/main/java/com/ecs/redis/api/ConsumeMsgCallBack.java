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

import com.ecs.redis.enums.ConsumeOrderlyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 消费消息
 *
 * @author zhanglinfeng
 */
public interface ConsumeMsgCallBack extends Serializable {
    Logger log = LoggerFactory.getLogger(ConsumeMsgCallBack.class);

    /**
     * 处理消息消费
     *
     * @param message
     */
    void handler(String message);


    /**
     * 消费消息
     *
     * @param message
     * @return
     */
    default ConsumeOrderlyStatus consumeMsg(String message) {
        try {
            handler(message);
            return ConsumeOrderlyStatus.SUCCESS;
        } catch (Exception e) {
            log.error("消息消费失败", e);
        }
        return ConsumeOrderlyStatus.ROLLBACK;
    }
}

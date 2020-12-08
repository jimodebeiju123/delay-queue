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
package com.ecs.redis.constant;

/**
 * 队列配置参数
 * @author zhanglinfeng
 */
public class QueueConfig {

    /**
     * 消费队列在无消息时阻塞超时时间，单位为秒
     * 默认为36个小时
     */
    private Long consumeBlocksTime = 60L*60L*48L;



    public Long getConsumeBlocksTime() {
        return consumeBlocksTime;
    }

    public void setConsumeBlocksTime(Long consumeBlocksTime) {
        this.consumeBlocksTime = consumeBlocksTime;
    }
}

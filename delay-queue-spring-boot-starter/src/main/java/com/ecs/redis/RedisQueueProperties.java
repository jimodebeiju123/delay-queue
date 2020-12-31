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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis 延迟队列配置信息
 * @author zhanglinfeng
 */
@ConfigurationProperties("redis.queue")
@Data
public class RedisQueueProperties {

    /**
     * 是否使用redis延迟队列
     */
    private Boolean enabled;
    /**
     * 是否启用消费者模式
     */
    private Boolean consumeEnabled;

    /**
     * 消费队列在无消息时阻塞超时时间，单位为秒
     * 默认为36个小时
     */
    private Long consumeBlocksTime = 60L*60L*36L;

    /**
     * 消息消费重试次数
     * 默认为3次
     */
    private Long retryNum = 3L;



}

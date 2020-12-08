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
package com.ecs.redis.handler;

import com.ecs.redis.annotation.RedisDelayQueueConsume;
import com.ecs.redis.api.ConsumeMsgCallBack;
import com.ecs.redis.constant.RedisKeyConstant;
import com.ecs.redis.utils.KeysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * com.ecs.redis.annotation.RedisDelayQueueConsume 处理程序
 *
 * @author zhanglinfeng
 */
@Slf4j
public class RedisDelayQueueConsumeHandler {


    /**
     * 处理队列处理类
     */
    public static void handler(ApplicationContext applicationContext) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RedisDelayQueueConsume.class);
        if (CollectionUtils.isEmpty(beans)) {
            log.warn("当前容器没有注解为:[{}]的处理类", RedisDelayQueueConsume.class);
            return;
        }
        for (Object value : beans.values()) {
            if (!(value instanceof ConsumeMsgCallBack)) {
                log.warn("当前处理类不是[{}]的子类，无法初始化!", ConsumeMsgCallBack.class);
                return;
            }
            RedisDelayQueueConsume consume = value.getClass().getAnnotation(RedisDelayQueueConsume.class);
            if (consume == null) {
                log.debug("当前处理类没有注解:[{}]", RedisDelayQueueConsume.class);
                return;
            }
            MsgHandlerContainer.addhandler(KeysUtils.getQueueKey(consume.queueName()), (ConsumeMsgCallBack) value);
        }
    }


}

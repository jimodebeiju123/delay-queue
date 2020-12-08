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
package com.ecs.redis.utils;

import com.ecs.redis.constant.RedisKeyConstant;
import com.ecs.redis.service.TransferMsgServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 消费节点工具类
 *
 * @author zhanglinfeng
 */
public class KeysUtils {
    private static Logger logger = LoggerFactory.getLogger(TransferMsgServiceImpl.class);

    private static String hostName = "defalut_hots";
    ;

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.warn("获取系统hostName失败!因此使用默认的hostName[{}]", hostName, e);
        }
    }

    /**
     * 获取处理节点的名称
     *
     * @param queueName
     * @return
     */
    public static String getHandlerNodeHostName(String queueName) {
        String queueNodeKey = getQueueNodeKey(queueName);
        return queueNodeKey + hostName;
    }

    /**
     * 获取队列处理节点的key
     *
     * @param queueName
     * @return
     */
    public static String getQueueNodeKey(String queueName) {
        return queueName + RedisKeyConstant.QUEUE_HOSTNAME_SUFFIX;
    }


    /**
     * 获取队列key
     *
     * @param queueName
     * @return
     */
    public static String getQueueKey(String queueName) {
        return "{" + queueName + "}" + RedisKeyConstant.QUEUE_SUFFIX;
    }


    /**
     * 获取队列处理类
     *
     * @param queueName
     * @return
     */
    public static String getQueueHandlerKey(String queueName) {
        return getQueueKey(queueName) + RedisKeyConstant.QUEUE_HANDLER_METHOD_SUFFIX;
    }

    /**
     * 获取队列集合的key
     *
     * @return
     */
    public static String getQueueSetKey() {
        return RedisKeyConstant.QUEUE_SET;
    }

    /**
     * 获取消息处理中的host
     *
     * @return
     */
    public static String getHandlerhost(String queueName) {
        return queueName + RedisKeyConstant.QUEUE_HANDLERHOST_SUFFIX;
    }
}

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
 * redis key 相关的常量
 * @author zhanglinfeng
 */
public class RedisKeyConstant {
    /**
     * 队列后缀
     */
    public static final String QUEUE_SUFFIX = "_QUEUE";

    /**
     * 处理节点后缀
     */
    public static final String QUEUE_HOSTNAME_SUFFIX = "_HOST_NAME";

    /**
     * 标示队列的集合
     */
    public static final String QUEUE_SET = "{QUEUE_SET}_ALL";
    /**
     * 队列处理方法的后缀
     */
    public static final String QUEUE_HANDLER_METHOD_SUFFIX = "_METHOD";
    /**
     * 消息处理中的host
     */
    public static final String QUEUE_HANDLERHOST_SUFFIX = "_handlerhost";

}

--
-- Created by IntelliJ IDEA.
-- User: zhanglinfeng
-- Date: 2020/12/6
-- Time: 21:01
-- To change this template use File | Settings | File Templates.
--
-- 队列的key
local queueName = KEYS[1]
-- 标示队列的集合
local queueSet =  KEYS[2]
local message = ARGV[1]
local msgScore =  ARGV[2]
-- 加入消息
redis.call('ZADD', queueName, msgScore,message)
-- 保存queue
redis.call('SADD', queueSet,queueName)



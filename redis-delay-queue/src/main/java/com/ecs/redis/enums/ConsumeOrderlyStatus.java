package com.ecs.redis.enums;


/**
 * 消息消费状态
 * @author zhanglinfeng
 */
public enum ConsumeOrderlyStatus {
    /**
     * 消息消费成功
     */
    SUCCESS,
    /**
     * 消息消费失败
     */
    ROLLBACK;
}

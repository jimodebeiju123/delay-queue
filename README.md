# redis-delay-queue
redis延时队列

基于redis的zset封装的延迟队列

## 使用方法 :  
1. mven 引入delay-queue-spring-boot-starter包
2. spring中注入bean RedisTemplate<String, String> redisTemplate
3. 配置文件增加redis.queue.enabled = true 来启用配置
4. 如果存在消息消费者则也需要设置redis.queue.consume-enabled = true 启用消费配置
5. 消息消费处理时,需要实现ConsumeMsgCallBack接口，并用注解 @RedisDelayQueueConsume(queueName = "test")来标示队列名称


## 发送消息：   
>  
      @Autowired
      private RedisQueueTemplate<String> redisQueueTemplate;
      
      // 发送消息 time 为具体的时间戳
      Long time = System.currentTimeMillis();
      redisQueueTemplate.sendMsg("queueName", "data", time);
      
      //发送消息 time 延迟多少为秒
      Long time = 2L;//延迟2秒
      redisQueueTemplate.sendMsgByDelayTime("queueName", "data", time);
>

## 接收消息
>
    @Service
    @RedisDelayQueueConsume(queueName = "test")
    public class RedisQueueConsume implements ConsumeMsgCallBack {
        private static final long serialVersionUID = 1816280011386253257L;

        /**
         * 处理消息消费
         *
         * @param message
         */
        @Override
        public void handler(String message) {
            log.info("开始处理消息:[{}]", message);
        }
    }
>
## 消息处理节点分组
系统启动时，自动获取当前节点可以处理的消息处理类，整个节点只处理有处理类的消息

## 后续计划
1. 增加死信队列处理逻辑
2. 增加广播模式
3. 增加其他类型的key和value
4. 去除RedisTemplate的依赖


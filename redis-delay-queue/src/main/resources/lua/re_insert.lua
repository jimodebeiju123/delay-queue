--
-- Created by IntelliJ IDEA.
-- User: zhanglinfeng
-- Date: 2020/12/6
-- Time: 23:04
-- To change this template use File | Settings | File Templates.
--
-- 拆分字符串
local function lua_string_split(str, split_char)
    local sub_str_tab = {};
    for mu_id in string.gmatch(str, '[^'..split_char..']+') do
        table.insert(sub_str_tab, mu_id)
    end
    return sub_str_tab;
end

--  将无法处理的消息再次放入到队列中
local handlerhostkey = KEYS[1]
-- 原始的消息所在的队列
local queueName = KEYS[2]
-- 传入当前时间
local time = tonumber(ARGV[1])


-- 获取满足要求的数据
local value1 = redis.call('ZRANGEBYSCORE', handlerhostkey, '-inf', time)

if (next(value1) == nil) then
    return
end

-- redis.log('3', "获取到的数据为:" .. value1[1])

for i, v in pairs(value1) do
    local msgLength = redis.call('LLEN', v)
    if (msgLength > 0) then
        local messages = redis.call('LRANGE',v, 0, msgLength)
        for n, m in pairs(messages) do
            local score =lua_string_split(m,",")
            redis.call('ZADD',queueName,tonumber(score[2]),score[1])
        end
        --删除key
        redis.call('DEL',v)
    end
end

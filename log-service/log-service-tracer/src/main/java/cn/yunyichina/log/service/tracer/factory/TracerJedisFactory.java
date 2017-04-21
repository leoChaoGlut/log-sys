package cn.yunyichina.log.service.tracer.factory;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.RedisRecordDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.util.ResponseUtil;
import cn.yunyichina.log.service.common.factory.JedisFactory;
import cn.yunyichina.log.service.tracer.client.RedisProxyClient;
import cn.yunyichina.log.service.tracer.exception.TracerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;

/**
 * Created by zhoufeng on 2017/4/20 0020.
 */
@Component
public class TracerJedisFactory extends JedisFactory {

    @Autowired
    RedisProxyClient redisProxyClient;

    @PreDestroy
    private void preDestory() {
        closeAllJedisPools();
    }

    @Override
    public Jedis getJedis(CollectedItemDO collectedItem) {
        Integer collectorId = collectedItem.getCollectorId();
        if (collectorId == null) {
            throw new TracerException("CollectorId is null");
        } else {
            JedisPool jedisPool = poolMap.get(collectorId);
            if (jedisPool == null) {
                try {
                    lock.lock();
                    jedisPool = poolMap.get(collectorId);
                    if (jedisPool == null) {
                        ResponseBodyDTO<RedisRecordDO> respDTO = redisProxyClient.redisRecord(collectedItem);
                        RedisRecordDO redisRecord = ResponseUtil.getResult(respDTO);
                        Jedis jedis = createJedisPoolAndReturnJedis(collectorId, redisRecord);
                        return jedis;
                    } else {
                        return jedisPool.getResource();
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                return jedisPool.getResource();
            }
        }
    }
}

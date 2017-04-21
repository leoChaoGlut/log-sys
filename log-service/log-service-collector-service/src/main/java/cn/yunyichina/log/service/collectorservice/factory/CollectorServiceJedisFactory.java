package cn.yunyichina.log.service.collectorservice.factory;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.RedisRecordDO;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.util.ResponseUtil;
import cn.yunyichina.log.service.collectorservice.client.RedisProxyClient;
import cn.yunyichina.log.service.collectorservice.exception.CollectorServiceException;
import cn.yunyichina.log.service.common.factory.JedisFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PreDestroy;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/20 10:17
 * @Description:
 */
@Component
public class CollectorServiceJedisFactory extends JedisFactory {

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
            throw new CollectorServiceException("CollectorId is null");
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

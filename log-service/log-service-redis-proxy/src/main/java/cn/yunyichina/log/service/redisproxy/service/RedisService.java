package cn.yunyichina.log.service.redisproxy.service;

import cn.yunyichina.log.common.entity.do_.CollectedItemDO;
import cn.yunyichina.log.common.entity.do_.RedisRecordDO;
import cn.yunyichina.log.service.redisproxy.exception.RedisProxyException;
import cn.yunyichina.log.service.redisproxy.mapper.RedisRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/4/13 15:50
 * @Description:
 */
@Service
public class RedisService {

    @Autowired
    RedisRecordMapper redisRecordMapper;

    public RedisRecordDO getRedisRecordBy(CollectedItemDO collectedItem) {
        Integer collectorId = collectedItem.getCollectorId();
        if (collectorId == null) {
            throw new RedisProxyException("CollectorId is null");
        } else {
            RedisRecordDO redisRecord = redisRecordMapper.selectLastestOne(collectorId);
            if (redisRecord == null) {
                throw new RedisProxyException("CollectorId:" + collectorId + " 对应的redisRecord 为 null");
            } else {
                return redisRecord;
            }
        }
    }

}
